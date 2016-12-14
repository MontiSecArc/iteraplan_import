package de.monticore.lang.montisecarc.generator.docker

import com.intellij.psi.PsiFile
import de.monticore.lang.montisecarc.generator.FileGenerator
import de.monticore.lang.montisecarc.generator.FreeMarker
import de.monticore.lang.montisecarc.generator.docker.data.DockerService
import de.monticore.lang.montisecarc.generator.docker.generator.LinkGenerator
import de.monticore.lang.montisecarc.generator.docker.generator.ServiceGenerator
import de.monticore.lang.montisecarc.psi.MSACompositeElementTypes
import java.io.InputStream

/**
 * Copyright 2016 Thomas Buning
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class DockerGenerator() : FileGenerator() {

    private val services = mutableSetOf<DockerService>()

    private val serviceLinks = mutableMapOf<String, List<String>>()

    override fun getSuffix(): String = ""

    override fun getExtension(): String = ".docker"

    override fun getDisplayName(): String = "Docker Generator"

    override fun registerGenerators(): DockerGenerator {

        registerGenerator(MSACompositeElementTypes.COMPONENT_DECLARATION, ServiceGenerator(), {

            if (it is DockerService) {

                services.add(it)
            }
        })

        registerGenerator(MSACompositeElementTypes.COMPONENT_INSTANCE_DECLARATION, ServiceGenerator(), {
            if (it is List<*>) {

                it.forEach {
                    if (it is DockerService) {

                        services.add(it)
                    }
                }
            }
        })

        registerGenerator(MSACompositeElementTypes.CONNECTOR, LinkGenerator(), {

            if (it is Pair<*,*>) {

                if (it.first is String && it.second is List<*>) {

                    val filter = (it.second as? List<Any?>)?.map { it as? String }?.requireNoNulls().orEmpty()

                    serviceLinks.put(it.first as String, filter)
                }
            }
        })

        return this
    }

    override fun aggregateResultFor(parsedFile: PsiFile): InputStream? {
        // Noop
        val serviceOutput = services.filter { service ->
            !service.imageName.isNullOrBlank() || serviceLinks.filter { it.key == service.serviceName }.isNotEmpty()
        }.map { service ->

            val variables = mutableMapOf<String, Any>()
            variables.put("serviceName", service.serviceName)
            variables.put("cpe", service.imageName.orEmpty())
            val linksForService = serviceLinks.filter { it.key == service.serviceName }.flatMap { it.value }.orEmpty()
            variables.put("links", linksForService)
            FreeMarker(this.javaClass).generateModelOutput("ToDockerCompose/DockerService.ftl", variables)
        }

        val compose = mutableMapOf<String, Any>()
        compose.put("services", serviceOutput)

        return FreeMarker(this.javaClass).generateModelOutput("ToDockerCompose/DockerCompose.ftl", compose).byteInputStream()
    }
}
package de.monticore.lang.montisecarc.generator.docker.generator

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import de.monticore.lang.montisecarc.generator.MSAGenerator
import de.monticore.lang.montisecarc.generator.docker.data.DockerService
import de.monticore.lang.montisecarc.psi.MSAComponentDeclaration
import de.monticore.lang.montisecarc.psi.MSAComponentInstanceDeclaration

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
class ServiceGenerator : MSAGenerator() {

    companion object {

        fun createComponentIdentifier(componentDeclaration: MSAComponentDeclaration): String? {

            if (componentDeclaration.instanceName.isNotEmpty()) {
                return componentDeclaration.qualifiedName.replace(".", "_") + "_" + componentDeclaration.instanceName
            }

            return null
        }

        fun createComponentInstanceIdentifier(msaComponentDeclaration: MSAComponentDeclaration, instanceName: String): String {

            return msaComponentDeclaration.qualifiedName.replace(".", "_") + "_" + instanceName
        }
    }

    override fun generate(psiElement: PsiElement): Any? {

        if (psiElement is MSAComponentDeclaration) {

            val componentIdentifier = createComponentIdentifier(psiElement)
            if (componentIdentifier != null) {

                if (psiElement.componentBody?.cpeStatementList.orEmpty().isNotEmpty()) {

                    val cpe = psiElement.componentBody!!.cpeStatementList.first().string?.text
                    if (!cpe.isNullOrEmpty()) {

                        return DockerService(componentIdentifier, cpe!!)
                    }
                }
                return DockerService(componentIdentifier, null)
            }
        } else if (psiElement is MSAComponentInstanceDeclaration) {

            val msaComponentName = psiElement.componentNameWithTypeProjectionList.last().componentName.references[0].resolve()

            val msaComponentDeclaration = PsiTreeUtil.getParentOfType(msaComponentName, MSAComponentDeclaration::class.java)
            if (msaComponentDeclaration != null && msaComponentDeclaration is MSAComponentDeclaration) {

                var cpe: String? = null
                if (msaComponentDeclaration.componentBody?.cpeStatementList.orEmpty().isNotEmpty()) {

                    cpe = msaComponentDeclaration.componentBody!!.cpeStatementList.first().string?.text
                }
                return psiElement.componentInstanceNameList.filter { it.name.isNotEmpty() }.map {

                    val componentInstanceIdentifier = createComponentInstanceIdentifier(msaComponentDeclaration, it.name)
                    DockerService(componentInstanceIdentifier, cpe)
                }
            }
        }
        return null
    }

}
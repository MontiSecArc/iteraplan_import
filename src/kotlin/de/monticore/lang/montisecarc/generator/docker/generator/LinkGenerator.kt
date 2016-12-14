package de.monticore.lang.montisecarc.generator.docker.generator

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import de.monticore.lang.montisecarc.generator.MSAGenerator
import de.monticore.lang.montisecarc.psi.*

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
class LinkGenerator : MSAGenerator() {

    private fun MSAQualifiedIdentifier.resolveLastInstanceName(): MSAComponentInstanceName? {

        if (this.componentInstanceNameList.isNotEmpty()) {

            val references = this.componentInstanceNameList.last().references

            if (references.isNotEmpty()) {

                val startComponentInstanceName = references[0].resolve()

                return startComponentInstanceName as? MSAComponentInstanceName
            }
        }
        return null
    }

    private fun MSAComponentInstanceName.getIdentifier(): String? {

        val signature = PsiTreeUtil.getParentOfType(this, MSAComponentSignature::class.java)
        val instanceDeclaration = PsiTreeUtil.getParentOfType(this, MSAComponentInstanceDeclaration::class.java)

        if (signature != null) {

            val declaration = PsiTreeUtil.getParentOfType(this, MSAComponentDeclaration::class.java)

            if (declaration != null) {
                return ServiceGenerator.createComponentIdentifier(declaration)
            }
        } else if (instanceDeclaration != null) {

            val msaComponentName = instanceDeclaration.componentNameWithTypeProjectionList.last().componentName.references[0].resolve()

            val msaComponentDeclaration = PsiTreeUtil.getParentOfType(msaComponentName, MSAComponentDeclaration::class.java)
            if (msaComponentDeclaration != null && msaComponentDeclaration is MSAComponentDeclaration) {

                return ServiceGenerator.createComponentInstanceIdentifier(msaComponentDeclaration, this.name)
            }
        }

        return null
    }

    override fun generate(psiElement: PsiElement): Any? {

        if (psiElement is MSAConnector) {

            val startComponentInstanceName = psiElement.connectSource.qualifiedIdentifier.resolveLastInstanceName()?.getIdentifier()
            if (!startComponentInstanceName.isNullOrEmpty()) {

                val targetList = psiElement.connectTargetList.map {

                    it.qualifiedIdentifier.resolveLastInstanceName()?.getIdentifier()
                }.filter {
                    !it.isNullOrEmpty()
                }.requireNoNulls()

                return Pair(startComponentInstanceName!!, targetList)
            }
        }
        return null
    }
}
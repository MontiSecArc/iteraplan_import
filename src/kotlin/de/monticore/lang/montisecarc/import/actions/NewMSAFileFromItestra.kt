package de.monticore.lang.montisecarc.import.actions

import com.intellij.ide.util.PackageChooserDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.psi.search.GlobalSearchScope
import de.monticore.lang.montisecarc.import.ItestraImporter

/**
 * Copyright 2016 thomasbuning
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
class NewMSAFileFromItestra : AnAction() {
    override fun actionPerformed(e: AnActionEvent?) {

        val chooseFile = FileChooser.chooseFile(FileChooserDescriptor(true, true, true, true, true, true), null, null)

        if (chooseFile != null) {

            val packageChooserDialog = PackageChooserDialog("Choose output package", e?.project)
            packageChooserDialog.show()
            val fileName = chooseFile.name.substringBeforeLast(".")
            val packageName = packageChooserDialog.selectedPackage.qualifiedName + "." + fileName

            val directories = packageChooserDialog.selectedPackage.getDirectories(GlobalSearchScope.projectScope(e?.project!!))

            if (directories.isNotEmpty()) {

                try {
                    directories[0].checkCreateSubdirectory(fileName)
                    directories[0].createSubdirectory(fileName)
                } catch (e: Exception) {
                }
                val path = directories[0].subdirectories.filter { it.name == fileName }.first().virtualFile.path

                ItestraImporter(chooseFile.inputStream, path, packageName).toMSAFiles()
            }
        }
    }
}
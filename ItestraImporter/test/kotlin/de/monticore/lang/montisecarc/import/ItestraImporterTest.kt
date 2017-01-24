package de.monticore.lang.montisecarc.import

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import java.io.File

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
@Tag("slow")
class ItestraImporterTest {

    @Test
    @DisplayName("Import Itestra from File")
    fun importTest(testInfo: TestInfo) {

        val packageIdentifier = "de.monticore.lang.montisecarc.graphquery.components"

        val file = File("/Development/RWTH/Masterarbeit/ItestraImporterPlugin/ItestraImporter/gen/${packageIdentifier.replace(".", "/")}")
        if (!file.exists()) {

            file.mkdirs()
        }

        val import = ItestraImporter(File("/Users/thomasbuning/Desktop/anonym.json"), file.path, packageIdentifier)
        import.toMSAFiles()
    }
}
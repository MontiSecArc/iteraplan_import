package de.monticore.lang.montisecarc.import

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
object Main {
    fun main(args: Array<String>) {

        if (args.isEmpty()) {
            println("Please provide an argument as a command-line argument")
            return
        }

        val filePath = args.find {
            it.contains("--filePath=")
        }.orEmpty().map {
            it.toString().substringAfterLast("=")
        }.first()

        val jsonString = args.find {
            it.contains("--jsonString=")
        }.orEmpty().map {
            it.toString().substringAfterLast("=")
        }.first()

        val packageName = args.find {
            it.contains("--package=")
        }?.map {
            it.toString().substringAfterLast("=")
        }?.first()

        val out = args.find {
            it.contains("--out=")
        }?.map {
            it.toString().substringAfterLast("=")
        }?.first()

        if (!filePath.isNullOrEmpty()) {

            ItestraImporter(File(filePath), out ?: "/", packageName ?: "demo").toMSAFiles()
        } else if (!jsonString.isNullOrEmpty()) {

            ItestraImporter(jsonString, out ?: "/", packageName ?: "demo").toMSAFiles()
        } else {

            printUsageAndExit()
        }
    }

    private fun printUsageAndExit() {
        println("""ItestraImporter
Usage: importer.jar <arguments>
where arguments may be one of:
    --filePath=<filePath>
    --jsonString=<jsonString>
    --package=<package>
    --out=<out>
""")
        System.exit(0)
    }
}
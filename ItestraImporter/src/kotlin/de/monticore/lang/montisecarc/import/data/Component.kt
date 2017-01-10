package de.monticore.lang.montisecarc.import.data

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

data class Component(val id: String, val foundName: String?, var trustLevel: Int?, var children: List<Component> = emptyList(), var connections: List<Connection> = emptyList(), var parent: Component? = null) {

    // Remove characters that cannot appear in a component name / instance name
    val name = foundName?.replace(Regex("[^a-zA-Z0-9-_]"), "")

    val typeName: String = name?.capitalize() ?: "T$id"
    val instanceName: String = name?.decapitalize() ?: "i$id"

    override fun equals(other: Any?): Boolean {
        return this.id == (other as? Component?)?.id
    }

    override fun toString(): String {
        return "Component(id='$id', name=$name, trustLevel=$trustLevel, typeName='$typeName', instanceName='$instanceName')"
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

fun Component.findCommonAncestor(other: Component): Component? {

    if (this == other) {
        return this
    }

    var firstParent: Component? = this
    var secondParent: Component? = other

    while (secondParent != null) {

        if (secondParent == firstParent) {

            return firstParent
        }

        val tempParent = firstParent?.parent
        while (firstParent != null) {

            if (secondParent == firstParent) {

                return firstParent
            }
            firstParent = firstParent.parent
        }
        firstParent = tempParent
        secondParent = secondParent.parent
    }

    return secondParent
}


fun Component.getPath(to: Component): String? = this.getPath(to, emptyList())?.map { it.instanceName }?.joinToString(".")

private fun Component.getPath(to: Component, path: List<Component>): List<Component>? {

    if (this == to) {

        return path
    }

    if (this.children.isEmpty()) {

        return null
    }

    val paths = this.children.map {

        it.getPath(to, path.plus(it))
    }

    val nonEmptyPaths = paths.filter { it != null }.requireNoNulls()

    if (nonEmptyPaths.isNotEmpty()) {
        return nonEmptyPaths.sortedBy { it.size }.first()
    } else {

        return null
    }
}

data class Connection(val from: Component, val to: Component)
package de.monticore.lang.montisecarc.import.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
data class Result(@SerializedName("id") val ids: List<Int>,
                  @SerializedName("name") val name: String,
                  @SerializedName("parent") val parents: List<Node>?,
                  @SerializedName("informationSystemRelease") val informationSystemRelease: List<Node>?,
                  @SerializedName("businessObject") val businessObject: List<Node>?,
                  @SerializedName("infrastructureElement") @Expose val infrastructureElements: List<Node>?,
                  @SerializedName("baseComponents") @Expose val baseComponents: List<Node>?,
                  @SerializedName("Tier") @Expose val tiers: List<String>?
                  )


fun Result.toComponent(): Component {

    val id = this.ids.first().toString()
    val trustLevel = this.tiers?.map {
        try {
            it.replace("Tier", "").toInt()
        } catch (ex: NumberFormatException) {
            null
        }
    }?.requireNoNulls()

    var trustLevelInt: Int? = null
    if (trustLevel != null && trustLevel.isNotEmpty()) {

        trustLevelInt = trustLevel.sorted().first()
    }

    return Component(id, this.name, trustLevelInt)
}

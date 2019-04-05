/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ir.ipack.ehsan.local.ipack

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import ir.ipack.ehsan.local.ipack.data.source.Repository
import ir.ipack.ehsan.local.ipack.mydata.MyDataViewModel
import ir.ipack.ehsan.local.ipack.myplan.MyPlanViewModel
import ir.ipack.ehsan.local.ipack.mytalk.MyTalkViewModel
import ir.ipack.ehsan.local.ipack.mytext.MyTextViewModel

class ViewModelFactory private constructor(
    private val application: Application,
    private val tasksRepository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MyPlanViewModel::class.java) ->
                    MyPlanViewModel(application, tasksRepository)
                isAssignableFrom(MyDataViewModel::class.java) ->
                    MyDataViewModel(application, tasksRepository)
                isAssignableFrom(MyTalkViewModel::class.java) ->
                    MyTalkViewModel(application, tasksRepository)
                isAssignableFrom(MyTextViewModel::class.java) ->
                    MyTextViewModel(application, tasksRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    application,
                    (application as MyApplication).repository
                ).also { INSTANCE = it }
            }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

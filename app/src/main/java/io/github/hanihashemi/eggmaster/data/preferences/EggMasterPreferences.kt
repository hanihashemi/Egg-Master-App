package io.github.hanihashemi.eggmaster.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.hanihashemi.eggmaster.data.models.EggDetailsDataModel
import io.github.hanihashemi.eggmaster.data.models.UserInfoDataModel
import javax.inject.Inject

class EggMasterPreferences @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val SHARED_PREFS_NAME = "EggMasterPrefs"
        private const val EGG_DETAILS_TEMP_KEY = "temperature"
        private const val EGG_DETAILS_SIZE_KEY = "size"
        private const val EGG_DETAILS_COUNT_KEY = "count"
        private const val EGG_DETAILS_BOILED_TYPE_KEY = "boiledType"
        private const val USER_INFO_STEP_KEY = "userStep"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun saveEggDetails(eggDetails: EggDetailsDataModel) = with(eggDetails) {
        sharedPreferences.edit()
            .putEnum(EGG_DETAILS_TEMP_KEY, temperature)
            .putEnum(EGG_DETAILS_SIZE_KEY, size)
            .putInt(EGG_DETAILS_COUNT_KEY, count)
            .putEnum(EGG_DETAILS_BOILED_TYPE_KEY, boiledType)
            .apply()
    }

    fun getEggDetails(): EggDetailsDataModel = EggDetailsDataModel(
        temperature = getEnum(EGG_DETAILS_TEMP_KEY, EggDetailsDataModel().temperature),
        size = getEnum(EGG_DETAILS_SIZE_KEY, EggDetailsDataModel().size),
        count = sharedPreferences.getInt(EGG_DETAILS_COUNT_KEY, EggDetailsDataModel().count),
        boiledType = getEnum(EGG_DETAILS_BOILED_TYPE_KEY, EggDetailsDataModel().boiledType),
    )

    fun saveUserInfo(userInfo: UserInfoDataModel) {
        sharedPreferences.edit().putEnum(USER_INFO_STEP_KEY, userInfo.userStep).apply()
    }

    fun getUserInfo(): UserInfoDataModel = UserInfoDataModel(
        userStep = getEnum(USER_INFO_STEP_KEY, UserInfoDataModel().userStep)
    )

    private fun Editor.putEnum(key: String, value: Enum<*>): Editor = putString(key, value.name)

    private inline fun <reified T : Enum<T>> getEnum(key: String, defaultValue: T): T {
        val name = sharedPreferences.getString(key, defaultValue.name)
        return enumValueOf(name ?: defaultValue.name)
    }
}

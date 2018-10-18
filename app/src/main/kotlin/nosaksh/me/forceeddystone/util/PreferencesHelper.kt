package nosaksh.me.forceeddystone.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * 汎用Preferenceヘルパー
 */
@Suppress("unused")
interface PreferencesHelper {

    /**
     * プリファレンス名
     */
    val preferencesName: String

    /**
     * キー値が存在するかどうか確認
     *
     * @param context コンテキスト
     * @param key キー名
     * @return 存在する場合はtrue
     */
    fun contains(context: Context, key: String): Boolean {
        val pref = getSharedPreferences(context)
        return pref.contains(key)
    }

    /**
     * キー値を削除
     *
     * @param context コンテキスト
     * @param key キー名
     */
    fun remove(context: Context, key: String) {
        val editor = getEditor(context)
        editor.remove(key)
        editor.commit()
    }

    /**
     * 文字列項目を取得
     *
     * @param context コンテキスト
     * @param key キー名
     * @param defValue 規定値
     * @return 取得失敗時は規定値を返す
     */
    fun getString(context: Context, key: String, defValue: String?): String? {
        val pref = getSharedPreferences(context)
        return pref.getString(key, defValue)
    }

    /**
     * 文字列項目を保存
     *
     * @param context コンテキスト
     * @param key キー名
     * @param value 値
     */
    fun putString(context: Context, key: String, value: String?) {
        val editor = getEditor(context)
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * 数値項目を取得
     *
     * @param context コンテキスト
     * @param key キー名
     * @param defValue 規定値
     * @return 取得失敗時は規定値を返す
     */
    fun getInt(context: Context, key: String, defValue: Int): Int {
        val pref = getSharedPreferences(context)
        return pref.getInt(key, defValue)
    }

    /**
     * 数値項目を保存
     *
     * @param context コンテキスト
     * @param key キー名
     * @param value 値
     */
    fun putInt(context: Context, key: String, value: Int) {
        val editor = getEditor(context)
        editor.putInt(key, value)
        editor.commit()
    }

    /**
     * 倍長数値項目を取得
     *
     * @param context コンテキスト
     * @param key キー名
     * @param defValue 規定値
     * @return 取得失敗時は規定値を返す
     */
    fun getLong(context: Context, key: String, defValue: Long): Long {
        val pref = getSharedPreferences(context)
        return pref.getLong(key, defValue)
    }

    /**
     * 倍長数値項目を保存
     *
     * @param context コンテキスト
     * @param key キー名
     * @param value 値
     */
    fun putLong(context: Context, key: String, value: Long) {
        val editor = getEditor(context)
        editor.putLong(key, value)
        editor.commit()
    }

    /**
     * 二値項目を取得
     *
     * @param context コンテキスト
     * @param key キー名
     * @param defValue 規定値
     * @return 取得失敗時は規定値を返す
     */
    fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        val pref = getSharedPreferences(context)
        return pref.getBoolean(key, defValue)
    }

    /**
     * 二値項目を保存
     *
     * @param context コンテキスト
     * @param key キー名
     * @param value 値
     */
    fun putBoolean(context: Context, key: String, value: Boolean) {
        val editor = getEditor(context)
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     * 文字列Set項目を取得
     *
     * @param context コンテキスト
     * @param key キー名
     * @param defValue 規定値
     * @return 取得失敗時は規定値を返す
     */
    fun getStringSet(context: Context, key: String,
                     defValue: Set<String>): MutableSet<String> {

        val pref = getSharedPreferences(context)

        val saveValue = pref.getStringSet(key, defValue)
        val returnSet = HashSet<String>(saveValue!!.size)
        for (saveValueItem in saveValue) {
            returnSet.add(saveValueItem)
        }

        return returnSet
    }

    /**
     * 文字列Set項目を保存
     *
     * @param context コンテキスト
     * @param key キー名
     * @param value 値
     */
    fun putStringSet(context: Context, key: String, value: String) {

        val saveValue = getStringSet(context, key, HashSet())
        saveValue.add(value)
        val editor = getEditor(context)
        editor.putStringSet(key, saveValue)
        editor.commit()
    }

    /**
     * 文字列Set項目を除去
     *
     * @param context コンテキスト
     * @param key キー名
     * @param value 値
     */
    fun removeStringSet(context: Context, key: String, value: String) {
        val saveValue = getStringSet(context, key, HashSet())
        saveValue.remove(value)
        val editor = getEditor(context)
        editor.putStringSet(key, saveValue)
        editor.commit()
    }

    /**
     * クリア
     *
     * @param context コンテキスト
     */
    fun removeAll(context: Context) {
        val editor = getEditor(context)
        editor.clear()
        editor.commit()
    }

    /**
     * SharedPreferenceを取得
     *
     * @param context コンテキスト
     * @return SharedPreferencesオブジェクト
     */
    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    /**
     * SharedPreferenceエディタを取得 取得後は必要に応じてcommitかapplyすること
     *
     * @param context コンテキスト
     * @return Editorオブジェクト
     */
    @SuppressLint("CommitPrefEdits")
    fun getEditor(context: Context): SharedPreferences.Editor {
        return getSharedPreferences(context).edit()
    }

}
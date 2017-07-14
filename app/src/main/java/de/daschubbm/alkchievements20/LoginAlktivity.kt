package de.daschubbm.alkchievements20

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import de.daschubbm.alkchievements20.dataManagement.LocalData
import kotlinx.android.synthetic.main.alktivity_login.*

class LoginAlktivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alktivity_login)
    }

    fun login(view: View) {
        val name = nameField.text.toString()
        if (name.matches(Regex("\\p{Lu}\\p{Ll}{2,26}( \\p{Lu}\\p{Ll}{2,26})?")) && name != "Anthony Modeste") {
            LocalData.saveS("name", name)

            val hansl = Intent(this, MainAlktivity::class.java)
            startActivity(hansl)
        } else {
            Toast.makeText(this, "Gib an gscheidn Nam ein, Affngsicht!", Toast.LENGTH_LONG).show()
        }
    }
}

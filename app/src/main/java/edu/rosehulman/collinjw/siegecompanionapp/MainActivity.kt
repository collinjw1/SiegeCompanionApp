package edu.rosehulman.collinjw.siegecompanionapp

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import androidx.fragment.app.Fragment
import edu.rosehulman.collinjw.siegecompanionapp.ui.gallery.GalleryFragment
import edu.rosehulman.collinjw.siegecompanionapp.ui.home.HomeFragment

class MainActivity : AppCompatActivity(), DirectoryFragment.OnDirectoryListener,
    HomeFragment.OnHomeListener, GalleryFragment.OnSearchListener {


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    override fun onDirectorySelected(searchTag: String) {
//        val postFragment = PostListFragment(searchTag)
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.nav_host_fragment, postFragment)
//        ft.addToBackStack("detail2")
//        ft.commit()
//    }

    override fun onDirectorySelected(searchTag: String) {
        val postFragment = PostListFragment(searchTag)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, postFragment)
        ft.addToBackStack("detail2")
        ft.commit()
    }

    override fun onButtonSelected(s: String) {
        val switchTo: Fragment
        if (s == Constants.SUBMIT) {
            switchTo = SubmitTipPage()
        } else if (s == Constants.HIGHLIGHT_REEL) {
            switchTo = PostListFragment(Constants.HIGHLIGHT_REEL)
        } else {
            switchTo = DirectoryFragment(s)
        }
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, switchTo)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onSearch(searchTag: String) {
        val postFragment = PostListFragment(searchTag)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, postFragment)
        ft.addToBackStack("detail")
        ft.commit()
    }



//    fun runDirectory() {
//        val dirFragment = DirectoryFragment()
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.nav_host_fragment, dirFragment)
//        ft.addToBackStack("detail")
//        ft.commit()
//
//    }
}

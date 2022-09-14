package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()

    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.onLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)
                // 2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }
//        findViewById<Button>(R.id.button).setOnClickListener{
//            Log.i("Ahmed","User clicked on button")
//        }

        loadItems()

        // Look up recycler view in the layout
        val recyclerView= findViewById<RecyclerView>(R.id.recycleView)
        adapter= TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Set up the button and input field, so that the user can enter a task and it's added to the list
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that the data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. Reset the text Field
            inputTextField.setText("")

            saveItems()
        }
    }
    // Save the data the user has inputted
    //Save data by writing and reading from a file


    //Get the file we need
    fun getDataFile(): File {

        //Every line is going to represent a specific task in out list of tasks
        return File(filesDir,"dataText")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(
                getDataFile(),
                Charset.defaultCharset()
            )
        } catch (ioException:IOException){
            ioException.printStackTrace()
        }
    }


    // Save items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}
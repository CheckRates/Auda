/*
 *	FILE			: TaskViewActivity.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Activity that allows the display of a task info
 *
 */

package com.github.mpagconestoga.mad_a01;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mpagconestoga.mad_a01.adapters.ViewSubtaskAdapter;
import com.github.mpagconestoga.mad_a01.objects.Category;
import com.github.mpagconestoga.mad_a01.objects.Person;
import com.github.mpagconestoga.mad_a01.objects.Task;
import com.github.mpagconestoga.mad_a01.repositories.CategoryRepository;
import com.github.mpagconestoga.mad_a01.viewmodel.TaskViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class TaskViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "TaskViewActivity";
    private TaskViewModel viewModel;
    private View backgroundView;

    // UI elements
    private TextView taskHeader;
    private ProgressBar progressBar;
    private TextView categoryHeader;
    private Button categoryLink;
    private TextView assignedPeopleList;
    private RecyclerView subtaskRecyclerView;
    private ViewSubtaskAdapter subtaskAdapter;

    private Task task = null;

    private GoogleMap map;


    // FUNCTION   : onCreate
    // DESCRIPTION: Initate UI Elements
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        // Set up UI elements
        progressBar = findViewById(R.id.task_progress);
        taskHeader = findViewById(R.id.task_title);
        categoryHeader = findViewById(R.id.category_help_header);
        categoryLink = findViewById(R.id.website_button);
        assignedPeopleList = findViewById(R.id.assigned_people_list);
        subtaskRecyclerView = findViewById(R.id.viewsubtask_list);

        CategoryRepository categoryRepository = new CategoryRepository(this.getApplication());
        subtaskAdapter = new ViewSubtaskAdapter(this, progressBar);

        // Grab viewModel and set background image place
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(TaskViewModel.class);
        int taskId = getIntent().getIntExtra("taskid", -1);
        backgroundView = findViewById(R.id.layout);

        // Get task
        viewModel.setTaskById(taskId);
        task = viewModel.getTask();

        // Setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.task_view_map);
        mapFragment.getMapAsync(this);

        // Set Task Header info
        taskHeader.setText(String.format("%s: %s", getString(R.string.task_header), task.getName()));

        Log.d(TAG, "onCreate: TASK LATTITUDE: " + task.getLattitude());
        Log.d(TAG, "onCreate: TASK LONGITUDE: " + task.getLongitude());

        // Set Assigned People display
        assignedPeopleList.setText(generatePeopleList(task.getAssignedPeople()));

        // Set Category Header and Link
        String categoryHelpHeader = task.getCategory().getName() + " " + getString(R.string.help_collon);
        final Category currentCategory = task.getCategory();
        currentCategory.setBackgroundURL(categoryRepository.getBackgroundURL(currentCategory.getName()));
        currentCategory.setWebURL(categoryRepository.getWebURL(currentCategory.getName()));

        categoryHeader.setText(categoryHelpHeader);
        categoryLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentCategory.getWebURL()));
                startActivity(browserIntent);
            }
        });

        // Set subtask recycler list
        subtaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subtaskRecyclerView.setHasFixedSize(true);

        // Subtask Adapter
        subtaskRecyclerView.setAdapter(subtaskAdapter);
        subtaskAdapter.setData(task.getSubtasks());

        // Logic for saving and loading background image
        String imageURL = currentCategory.getBackgroundURL();
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(imageURL);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(task.getLatLng(), 15));
        map.addMarker(new MarkerOptions().position(task.getLatLng()));
    }

    /*
     *   CLASS       : DownloadTask
     *   DESCRIPTION : Class responsible for downloading the background for the task view
     */
    class DownloadTask extends AsyncTask<String, Integer, String> {   //Integer and second String can be changed back to Void later

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(TaskViewActivity.this);
            progressDialog.setTitle("Download in Progress...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params){                      //change String back to void
            String path = params[0];                                             // assigns the url to the path variable
            int fileLength = 0;

            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                fileLength = urlConnection.getContentLength();                   //assigns length of file to variable
                File newFolder = getApplicationContext().getExternalFilesDir(null);         //assigns to folder for storage
                if (newFolder.exists())
                {
                    newFolder.mkdirs();                                           //creates folder should it not already exist
                }

                Log.d("filepath", getApplicationContext().getExternalFilesDir(null).toString());

                File inputFile = new File(newFolder, "downloaded_image.jpg");    // might need to change second string
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192); //opens an inputstream with an 8kb buffer
                byte[] data = new byte[1024];                                        // reads the info in at 1kb
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(inputFile);
                while((count=inputStream.read(data))!=-1){                           //I think this is all for the progress bar and can be removed
                    total += count;
                    outputStream.write(data, 0, count);
                    int progress = total * 100/fileLength;                           //for the progress bar; can remove
                    publishProgress(progress);
                }
                inputStream.close();
                outputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download Complete...";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {                        // consider changing from Integer back to void
            progressDialog.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String result) {                                  //change parameters back to Void aVoid
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            SetBackground setBackground = new SetBackground();
            setBackground.execute();
        }
    }


    /*
     *   CLASS       : SetBackground
     *   DESCRIPTION : Class responsible for setting the background image for the the task view
     */
    class SetBackground extends AsyncTask<Void, Void, BitmapDrawable> {

        @Override
        protected BitmapDrawable doInBackground(Void... voids) {

            String path = getApplicationContext().getExternalFilesDir(null) + File.separator + "downloaded_image.jpg";
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), bitmap);

            return bitmapDrawable;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
            backgroundView.setBackground(bitmapDrawable);
            bitmapDrawable.setAlpha(50);
        }
    }

    // FUNCTION   : generatePeopleList
    // DESCRIPTION: Generates a string for displaying the task's assigned people in the view
    public static String generatePeopleList(List<Person> personList) {
        int size = personList.size();
        StringBuilder returnString = new StringBuilder();

        for (int i = 0; i < size ; i++) {
            returnString.append(personList.get(i).getName());
            if(i != size - 1) {
                returnString.append(" ,");
            }
        }
        return returnString.toString();
    }
}

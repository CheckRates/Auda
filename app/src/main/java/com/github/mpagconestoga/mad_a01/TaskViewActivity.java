/*
 *	FILE			: TaskViewActivity.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Activity that allows the display of a task info
 *
 */

package com.github.mpagconestoga.mad_a01;


import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mpagconestoga.mad_a01.adapters.ViewSubtaskAdapter;
import com.github.mpagconestoga.mad_a01.objects.Category;
import com.github.mpagconestoga.mad_a01.objects.ConnectionBroadcastReceiver;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "TaskViewActivity";
    private TaskViewModel viewModel;
    private TaskTimerService timerService;
    private View backgroundView;
    ConnectionBroadcastReceiver connectionBroadcastReceiver = new ConnectionBroadcastReceiver();


    // UI elements
    private TextView taskHeader;
    private ProgressBar progressBar;
    private TextView categoryHeader;
    private Button categoryLink;
    private TextView assignedPeopleList;
    private RecyclerView subtaskRecyclerView;
    private ViewSubtaskAdapter subtaskAdapter;
    private Button calendarLink;

    @Override
    // FUNCTION   : onStart
    // DESCRIPTION: Initiates the dynamic broadcast receiver.
    //              Triggered when app is in foreground.
    protected void  onStart(){
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionBroadcastReceiver, filter);
    }
    private Button timerButton;
    private TextView timerView;

    long totalTime;

    private static final int REQUEST_CODE = 1;
    private String imageURL;

    @Override
    // FUNCTION   : onStop
    // DESCRIPTION: Deactivates the dynamic broadcast receiver.
    //              Triggered when app is in foreground.
    protected void  onStop(){
        super.onStop();
        unregisterReceiver(connectionBroadcastReceiver);
    }

    private Task task = null;

    private GoogleMap map;

    // FUNCTION   : onCreate
    // DESCRIPTION: Initiate UI Elements
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        totalTime = 0;
        // Set up UI elements
        progressBar = findViewById(R.id.task_progress);
        taskHeader = findViewById(R.id.task_title);
        categoryHeader = findViewById(R.id.category_help_header);
        categoryLink = findViewById(R.id.website_button);
        calendarLink = findViewById(R.id.calendar_button);
        assignedPeopleList = findViewById(R.id.assigned_people_list);
        subtaskRecyclerView = findViewById(R.id.viewsubtask_list);
        timerButton = findViewById(R.id.start_timer_button);
        timerView = findViewById(R.id.timer_view);


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


        final String name = task.getName();
        final Date date = task.getEndTime();
        calendarLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToCalendar(name, date);
            }
        });


        // Set subtask recycler list
        subtaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subtaskRecyclerView.setHasFixedSize(true);

        // Get service
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleUpdates();
            }
        });

        viewModel.getBinder().observe(this, new Observer<TaskTimerService.ServBinder>() {
            @Override
            public void onChanged(@Nullable TaskTimerService.ServBinder servBinder) {
                if(servBinder != null) {
                    Log.d(TAG, "InChanged: connected to the service");
                    timerService = servBinder.getService();
                }
                else {
                    Log.d(TAG, "InChanged: unbound from the service");
                    timerService = null;
                }
            }
        });

        viewModel.getIsProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean aBoolean) {
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (aBoolean) {
                            if (viewModel.getBinder().getValue() != null) {
                                if (timerService.getElapsedTime() == timerService.getMaxHours()) {
                                    viewModel.setIsUpdating(false);
                                }
                                handler.postDelayed(this, 100);
                            }
                        }
                        else {
                            handler.removeCallbacks(this );
                        }
                    }
                };

                if(aBoolean) {
                    timerButton.setText(getString(R.string.pause));
                    handler.postDelayed(runnable, 100);
                }

                else {
                    if(timerService.getElapsedTime() == timerService.getMaxHours()) {
                        timerButton.setText(getString(R.string.overtime));
                    }
                    else {
                        timerButton.setText(getString(R.string.resume));
                    }
                }
            }
        });

        // Subtask Adapter
        subtaskRecyclerView.setAdapter(subtaskAdapter);
        subtaskAdapter.setData(task.getSubtasks());
        subtaskAdapter.getTaskDone().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
            if(timerService != null) {
                Log.d(TAG, "&--> Task Done");
                int hours = timerService.getElapsedTime() / 3600;
                int minutes = (timerService.getElapsedTime() - (hours * 3600)) / 60;
                Log.d(TAG, "&--> Time in seconds: " + timerService.getElapsedTime());
                String progress = " " + (hours) + ":";
                if (hours > 5) {
                    progress = " "+ getString(R.string.more_5_hours);
                } else {
                    progress += minutes;
                    if (hours == 0 && minutes < 10) {
                        progress = " " + getString(R.string.less_minutes);
                    }
                }
                timerView.setText(progress);
                timerService.pauseTaskTimer();
                timerService.resetTimer();
                timerButton.setText(getString(R.string.task_done));
            }
            }
        });


        // Logic for saving and loading background image
        imageURL = currentCategory.getBackgroundURL();
        verifyUserPermissions();

    }
    /*
     *    METHOD      :     verifyUserPermissions
     *    DESCRIPTION :     Ask for permissions. Need permissions to download image to storage
     *                      in the task view screen. Downloaded image is loaded as background image
     *    PARAMETERS  :
     *    RETURNS     :     VOID
     * */
    private void verifyUserPermissions() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //check if permissions are granted
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permission[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permission[1]) == PackageManager.PERMISSION_GRANTED) {

            //if permissions are granted then download image and load as background
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(imageURL);
        } else {
            //do nothing
        }
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


    // FUNCTION   : onAddToCalendar
    // DESCRIPTION: Creates an event inside the system calendar
    public void onAddToCalendar(String title, Date date) {
        Intent calendar = new Intent(Intent.ACTION_INSERT);
        calendar.setType("vnd.android.cursor.item/event");
        long dueDate = date.getTime(); //Get the due date time

        calendar.setData(CalendarContract.Events.CONTENT_URI);
        calendar.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dueDate); //Insert the task Due Date
        calendar.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true); //For an all day event

        calendar.putExtra(CalendarContract.Events.TITLE, title); //Name of the Task

        startActivity(calendar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(viewModel.getBinder() != null) {
            unbindService(viewModel.getServiceConnection());
        }
    }

    private void startTimer() {
        Intent serviceIntent = new Intent(this, TaskTimerService.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService() {
        Intent serviceIntent = new Intent(this, TaskTimerService.class);
        bindService(serviceIntent, viewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    private void toggleUpdates() {
        if(timerService != null) {
            if(timerService.getElapsedTime() == timerService.getMaxHours()) {
                timerButton.setText(getString(R.string.start));
            }
            else {
                if(timerService.getIsPaused()) {
                    timerService.resumeTaskTimer();
                    viewModel.setIsUpdating(true);
                }
                else {
                    timerService.pauseTaskTimer();
                    viewModel.setIsUpdating(false);
                }
            }
        }
    }
}

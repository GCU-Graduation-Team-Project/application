package com.example.myapplication.ui.fragments.CameraFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FallbackStrategy;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCamera3Binding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CameraFragment3 extends Fragment {
    FragmentCamera3Binding binding;
    private boolean isRecording = false;
    private Recording recording;
    private VideoCapture<Recorder> videoCapture;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String videoUrl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCamera3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);


        binding.recordButton.setOnClickListener(v -> {
            if (!isRecording) {
                startRecording();
            } else {
                stopRecording();
            }
        });

        checkAndRequestPermissions();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


    }

    private void checkAndRequestPermissions() {
        boolean allPermissionsGranted = true;
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (allPermissionsGranted) {
            initializeCamera();
        } else {
            requestPermissionsLauncher.launch(REQUIRED_PERMISSIONS);
        }
    }

    // 필요한 권한 배열
    private String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private final ActivityResultLauncher<String[]> requestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    permissions -> {
                        boolean allGranted = true;
                        for (Boolean isGranted : permissions.values()) {
                            if (!isGranted) {
                                allGranted = false;
                                break;
                            }
                        }

                        if (allGranted) {
                            initializeCamera();
                        } else {
                            Toast.makeText(getContext(), "카메라와 오디오 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });


    private void initializeCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

                // QualitySelector 설정
                QualitySelector qualitySelector = QualitySelector.from(
                        Quality.HD,
                        FallbackStrategy.higherQualityOrLowerThan(Quality.SD));

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(qualitySelector)
                        .build();

                videoCapture = VideoCapture.withOutput(recorder);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        getViewLifecycleOwner(),
                        cameraSelector,
                        preview,
                        videoCapture
                );

            } catch (Exception e) {
                Log.e("CameraFragment3", "Camera initialization failed", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    @SuppressLint("MissingPermission")
    private void startRecording() {
        if (videoCapture == null) return;

        String fileName = "Recording-" + System.currentTimeMillis() + ".mp4";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

        MediaStoreOutputOptions options = new MediaStoreOutputOptions.Builder(
                requireContext().getContentResolver(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues)
                .build();


        recording = videoCapture.getOutput().prepareRecording(requireContext(), options)
                .withAudioEnabled()
                .start(ContextCompat.getMainExecutor(requireContext()), videoRecordEvent -> {
                    if (videoRecordEvent instanceof VideoRecordEvent.Start) {
                        isRecording = true;
                        binding.recordButton.setText("녹화 중지");
                    }
                    if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                        VideoRecordEvent.Finalize finalizeEvent = (VideoRecordEvent.Finalize) videoRecordEvent;

                        if (!((VideoRecordEvent.Finalize) videoRecordEvent).hasError()) {
                            Uri videoUri = finalizeEvent.getOutputResults().getOutputUri();
                            uploadVideoToFirebase(videoUri, fileName);
                        } else {
                            recording.close();
                            recording = null;
                            Log.e("CameraFragment3", "Video capture failed: " +
                                    ((VideoRecordEvent.Finalize) videoRecordEvent).getError());
                        }
                    }
                });
    }

    private void stopRecording() {
        if (recording != null) {
            recording.stop();
            recording = null;
        }
        isRecording = false;
        binding.recordButton.setText("녹화 시작");
    }

    private void uploadVideoToFirebase(Uri videoUri, String fileName) {
        // Firebase Storage에 업로드할 경로 설정
        StorageReference videoRef = storageRef.child("videos/" + fileName);

        try {
            // Uri에서 실제 파일 경로 가져오기
            String realPath = getRealPathFromUri(videoUri);
            File videoFile = new File(realPath);

            // 업로드 시작
            UploadTask uploadTask = videoRef.putFile(Uri.fromFile(videoFile));

            // 업로드 상태 모니터링
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // 업로드 성공
                videoRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    videoUrl = downloadUri.toString();
                    Log.d("Upload", "Upload success. URL: " + videoUrl);
                    sendDataToServer();
                });
            }).addOnFailureListener(e -> {
                // 업로드 실패
                Log.e("Upload", "Upload failed", e);
                Toast.makeText(requireContext(), "동영상 업로드 실패", Toast.LENGTH_SHORT).show();
            });

        } catch (Exception e) {
            Log.e("Upload", "Error preparing upload", e);
            Toast.makeText(requireContext(), "업로드 준비 중 오류 발생", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromUri(Uri contentUri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        CursorLoader loader = new CursorLoader(requireContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void sendDataToServer() {
        new Thread(() -> {
            try {
                String encodedVideoUri = URLEncoder.encode(videoUrl, "UTF-8");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("videoPath", encodedVideoUri);

                URL url = new URL("http://34.64.206.14:8000/process");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                Log.d("JSON Payload", jsonObject.toString());
                int responseCode = connection.getResponseCode();

                Log.d("ServerResponse", "Response Code: " + responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Fragment nextFragment = new CameraFragment4();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
        );

        transaction.replace(R.id.fragment_container, nextFragment);
        transaction.commit();

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRecording) {
            stopRecording();
        }
    }

}

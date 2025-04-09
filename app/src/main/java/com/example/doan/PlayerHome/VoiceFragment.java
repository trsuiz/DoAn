package com.example.doan.PlayerHome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import com.example.doan.R;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceFragment extends Fragment {
    private SpeechRecognizer speechRecognizer;
    private TextView textSample, textResult, textFeedback;
    private Button btnStartSpeaking, btnAboutYourself1, btnAboutYourself2, btnAboutYourself3, btnAboutYourself4, btnAboutYourself5;
    private String sampleSentence;
    private final ArrayList<String> sentenceList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voice, container, false);

        // ✅ Kiểm tra hỗ trợ giọng nói
        boolean isAvailable = SpeechRecognizer.isRecognitionAvailable(requireContext());
        Toast.makeText(requireContext(), isAvailable ? "✅ Hỗ trợ giọng nói" : "❌ Không hỗ trợ!", Toast.LENGTH_LONG).show();

        textSample = rootView.findViewById(R.id.textSample);
        textResult = rootView.findViewById(R.id.textResult);
        textFeedback = rootView.findViewById(R.id.textFeedback);
        btnStartSpeaking = rootView.findViewById(R.id.btnStartSpeaking);
        btnAboutYourself1 = rootView.findViewById(R.id.btnAboutYourself1);
        btnAboutYourself2 = rootView.findViewById(R.id.btnAboutYourself2);
        btnAboutYourself3 = rootView.findViewById(R.id.btnAboutYourself3);
        btnAboutYourself4 = rootView.findViewById(R.id.btnAboutYourself4);
        btnAboutYourself5 = rootView.findViewById(R.id.btnAboutYourself5);
        Button btnBack = rootView.findViewById(R.id.btnBackToLesson);
        LinearLayout layoutQuestionButtons = rootView.findViewById(R.id.layoutQuestionButtons);

        View lessonLayout = rootView.findViewById(R.id.lessonLayout);
        View voiceLayout = rootView.findViewById(R.id.voiceLayout);
        // 👉 Thêm câu mẫu
        addSampleSentences();

        // 👉 Random câu mẫu mỗi lần vào bài học
        sampleSentence = sentenceList.get((int)(Math.random() * sentenceList.size()));
        textSample.setText(sampleSentence);
        layoutQuestionButtons.setVisibility(View.VISIBLE);

        // Đảm bảo câu mẫu được hiển thị
        btnBack.setOnClickListener(v -> {
            voiceLayout.setVisibility(View.GONE);              // Ẩn giao diện voice
            lessonLayout.setVisibility(View.VISIBLE);          // Hiện lại lesson layout
            layoutQuestionButtons.setVisibility(View.VISIBLE); // Hiện lại các nút câu hỏi
        });

        // Gắn sự kiện cho tất cả các nút (ví dụ chung)
        btnAboutYourself1.setOnClickListener(v -> {
            sampleSentence = "Hello";
            textSample.setText(sampleSentence);
            lessonLayout.setVisibility(View.GONE);
            voiceLayout.setVisibility(View.VISIBLE);
            layoutQuestionButtons.setVisibility(View.GONE);
        });

        btnAboutYourself2.setOnClickListener(v -> {
            sampleSentence = "What is your name";
            textSample.setText(sampleSentence);
            lessonLayout.setVisibility(View.GONE);
            voiceLayout.setVisibility(View.VISIBLE);
            layoutQuestionButtons.setVisibility(View.GONE);
        });

        btnAboutYourself3.setOnClickListener(v -> {
            sampleSentence = "How old are you";
            textSample.setText(sampleSentence);
            lessonLayout.setVisibility(View.GONE);
            voiceLayout.setVisibility(View.VISIBLE);
            layoutQuestionButtons.setVisibility(View.GONE);
        });

        btnAboutYourself4.setOnClickListener(v -> {
            sampleSentence = "Where are you from?";
            textSample.setText(sampleSentence);
            lessonLayout.setVisibility(View.GONE);
            voiceLayout.setVisibility(View.VISIBLE);
            layoutQuestionButtons.setVisibility(View.GONE);
        });

        btnAboutYourself5.setOnClickListener(v -> {
            sampleSentence = "Do you like singing?";
            textSample.setText(sampleSentence);
            lessonLayout.setVisibility(View.GONE);
            voiceLayout.setVisibility(View.VISIBLE);
            layoutQuestionButtons.setVisibility(View.GONE);
        });

        checkPermission();

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String spokenText = matches.get(0);
                    textResult.setText(spokenText);
                    checkAnswer(spokenText);
                }
            }

            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override
            public void onEvent(int eventType, Bundle params) {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onError(int error) {
                String message;
                switch (error) {
                    case SpeechRecognizer.ERROR_AUDIO: message = "🎤 Lỗi âm thanh!"; break;
                    case SpeechRecognizer.ERROR_CLIENT: message = "⚠️ Lỗi từ client!"; break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: message = "🚫 Thiếu quyền ghi âm!"; break;
                    case SpeechRecognizer.ERROR_NETWORK:
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT: message = "🌐 Lỗi mạng!"; break;
                    case SpeechRecognizer.ERROR_NO_MATCH: message = "❌ Không nhận diện được giọng nói!"; break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY: message = "⏳ Bộ nhận diện đang bận!"; break;
                    case SpeechRecognizer.ERROR_SERVER: message = "🖥️ Lỗi từ server!"; break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT: message = "⏳ Không có âm thanh đầu vào!"; break;
                    default: message = "⚠️ Lỗi không xác định!"; break;
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                Log.e("SpeechError", "Mã lỗi: " + error + " - " + message);
            }
        });

        // Khi nhấn nút, bắt đầu nhận diện giọng nói
        btnStartSpeaking.setOnClickListener(v -> startSpeechRecognition());
        return rootView;
    }
    // 👉 Danh sách câu mẫu
    private void addSampleSentences() {
        sentenceList.add("Hello");
        sentenceList.add("What is your name");
        sentenceList.add("How old are you");
        sentenceList.add("Where are you from?");
        sentenceList.add("Do you like singing?");
    }

    // Hàm kiểm tra câu nói đúng hay sai
    private void checkAnswer(String spokenText) {
        // Chuẩn hoá cả câu mẫu và câu nói
        String user = spokenText.trim().toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
        String sample = sampleSentence.trim().toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
        if (user.contains(sample)) {
            textFeedback.setText("🧠 Chuẩn không cần chỉnh, chỉnh là sai luôn!");
            textFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            textFeedback.setText("❌ Sai rồi... giống như lúc bạn tỏ tình mà crush chỉ 'haha' 😔");
            textFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    // Hàm bắt đầu nhận diện giọng nói
    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy đọc câu mẫu...");
        speechRecognizer.startListening(intent);

    }


    // Kiểm tra và yêu cầu quyền ghi âm
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}

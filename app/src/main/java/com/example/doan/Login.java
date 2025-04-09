package com.example.doan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.Admin.Admin_panel;
import com.example.doan.PlayerHome.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginBtn;
    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView togglePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(v -> loginUser());


        DatabaseHelper db = new DatabaseHelper(this);
        togglePassword = findViewById(R.id.show_password_toggle);

        togglePassword.setOnClickListener(v -> {
            if (passwordInput.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Hiện mật khẩu
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePassword.setImageResource(R.drawable.ic_eye_open); // icon con mắt mở
            } else {
                // Ẩn mật khẩu
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePassword.setImageResource(R.drawable.ic_eye_closed); // icon con mắt đóng
            }
            passwordInput.setSelection(passwordInput.getText().length()); // Giữ con trỏ ở cuối
        });
        /*db.clearAllData();
        db.insertSampleData();*/
        db.logAllDatabaseData();

        TextView forgotPasswordText = findViewById(R.id.forgot_password_text);
        forgotPasswordText.setOnClickListener(v -> showForgotPasswordDialog());






        VideoView videoView = findViewById(R.id.videoBackground);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_login);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // Lặp lại video
            //mp.setVolume(0, 0);
        });
        videoView.start();





        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Lấy từ google-services.json
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

// Gán sự kiện cho nút đăng nhập Google
        findViewById(R.id.google_login_btn).setOnClickListener(v -> signInWithGoogle());




    }
    private void showForgotPasswordDialog() {
        // Tạo một EditText để nhập email
        final EditText emailInput = new EditText(this);
        emailInput.setHint("example@gmail.com");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setPadding(50, 40, 50, 40);


        // Lấy sẵn email đang có nếu người dùng đã nhập rồi
        String preFilledEmail = usernameInput.getText().toString().trim();
        if (!preFilledEmail.isEmpty()) {
            emailInput.setText(preFilledEmail);
        }

        // Tạo AlertDialog đẹp hơn
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("🔐 Quên mật khẩu?")
                .setMessage("Nhập email của bạn để nhận liên kết đặt lại mật khẩu:")
                .setView(emailInput)
                .setPositiveButton("Gửi", (dialog, which) -> {
                    String email = emailInput.getText().toString().trim();
                    if (!email.isEmpty()) {
                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "✔ Đã gửi email đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Login.this, "❌ Không thể gửi email. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Login.this, "⚠ Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }



    private void loginUser() {
        String email = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Ràng buộc email không được để trống
        if (email.isEmpty()) {
            usernameInput.setError("Vui lòng nhập email");
            usernameInput.requestFocus();
            return;
        }

        // Kiểm tra định dạng email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameInput.setError("Email không hợp lệ");
            usernameInput.requestFocus();
            return;
        }

        // Ràng buộc mật khẩu không được để trống
        if (password.isEmpty()) {
            passwordInput.setError("Vui lòng nhập mật khẩu");
            passwordInput.requestFocus();
            return;
        }

        // Mật khẩu tối thiểu 6 ký tự
        if (password.length() < 6) {
            passwordInput.setError("Mật khẩu phải có ít nhất 6 ký tự");
            passwordInput.requestFocus();
            return;
        }

        // Nếu hợp lệ thì tiếp tục đăng nhập
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserData(user);
                            if (email.equals("admin@gmail.com")) {
                                navigateToAdmin();
                            } else {
                                navigateToHome();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserData(user);
                        navigateToHome();
                    } else {
                        Toast.makeText(this, "Firebase auth failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void saveUserData(FirebaseUser user) {
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FULL_NAME", user.getDisplayName());
        editor.putString("USERNAME", user.getEmail());

        // Initialize score to 0 for new user login (or retrieve from SharedPreferences if already exists)
        if (!preferences.contains("USER_SCORE")) {
            editor.putInt("USER_SCORE", 0); // Setting score to 0 if this is the user's first time logging in
        }

        editor.apply();
    }

    private void navigateToHome() {
        Intent intent = new Intent(Login.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAdmin() {
        Intent intent = new Intent(Login.this, Admin_panel.class);
        startActivity(intent);
        finish();
    }

    public void redirectToRegister(View view) {
        startActivity(new Intent(Login.this, Register.class));
        finish();
    }



    /*

    private void checkUserRole(FirebaseUser user) {
        String email = user.getEmail();

        // Giả sử tài khoản Admin có email cố định
        if (email != null && email.equals("admin@gmail.com")) {
            Intent intent = new Intent(Login.this, Admin_panel.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Login.this, HomeActivity.class);
            startActivity(intent);
        }
        finish();
    }

     */
}

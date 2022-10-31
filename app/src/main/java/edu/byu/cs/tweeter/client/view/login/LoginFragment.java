package edu.byu.cs.tweeter.client.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.presenter.authenticate.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticateView;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the login screen.
 */
public class LoginFragment extends Fragment implements AuthenticateView {
    private static final String LOG_TAG = "LoginFragment";
    private final LoginPresenter presenter = new LoginPresenter(this);
    private Toast infoToast;
    private EditText alias;
    private EditText passwordEditText;
    private TextView errorView;

    /**
    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @return the fragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        alias = view.findViewById(R.id.loginUsername);
        passwordEditText = view.findViewById(R.id.loginPassword);
        errorView = view.findViewById(R.id.loginError);
        Button loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username = alias.getText().toString();
                String password = passwordEditText.getText().toString();
                presenter.initiateLogin(username, password);
            }
        });

        return view;
    }

    @Override
    public void displayInfoMessage(String message) {
        clearInfoMessage();
        infoToast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        infoToast.show();
    }

    private void clearInfoMessage() {
        if (infoToast != null) {
            infoToast.cancel();
            infoToast = null;
        }
    }

    @Override
    public void displayErrorMessage(String message) {
        errorView.setText(message);
    }

    @Override
    public void clearValidationMessage() {
        errorView.setText("");
    }

    @Override
    public void navigateToUser(User user) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
        startActivity(intent);
    }
}

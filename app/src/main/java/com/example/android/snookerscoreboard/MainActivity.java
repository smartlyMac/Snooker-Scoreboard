package com.example.android.snookerscoreboard;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    int scoreAOld, scoreACurrent, scoreBOld, scoreBCurrent, frameScoreA, frameScoreB = 0;
    int pointsDifference = 0;
    int pointsAvailable = 147;
    int numberOfReds = 15;
    int potValue, foulValue;
    int yellowAvailable = 2;
    int greenAvailable = 3;
    int brownAvailable = 4;
    int blueAvailable = 5;
    int pinkAvailable = 6;
    int blackAvailable = 7;
    int targetBallOld, targetBallCurrent;
    int frameNumber = 1;

    TextView playerAName, playerBName, scoreViewA, scoreViewB, frameScoreViewA, frameScoreViewB, bestOfX, ptsDiff, ptsAvl;
    RadioButton switchA, switchB;
    Switch freeBallSwitch;
    Boolean lastPointsFouled, finalRed;
    ImageButton redBall, yellowBall, greenBall, brownBall, blueBall, pinkBall, blackBall;

    AlertDialog.Builder builder;
    View welcomeView, breakView, resetView, endFrameView, frameSummaryView, matchSummaryView;
    AlertDialog welcomeDialog, breakDialog, resetDialog, endFrameDialog, frameSummaryDialog, matchSummaryDialog;
    EditText enterPlayerA, enterPlayerB;
    Spinner matchFormat;
    int matchFormatInt;
    String nameAEntered, nameBEntered, matchFormatChosen;
    RadioButton breakA, breakB;
    String nextBreaker;

    TextView frameSummaryTitle, nextBreakerView, frameWinner, frameScoreSummary;
    TextView matchWinner, matchScoreSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerAName = findViewById(R.id.playerAName);
        playerBName = findViewById(R.id.playerBName);
        scoreViewA = findViewById(R.id.scoreA);
        scoreViewB = findViewById(R.id.scoreB);
        frameScoreViewA = findViewById(R.id.frameScoreA);
        frameScoreViewB = findViewById(R.id.frameScoreB);
        bestOfX = findViewById(R.id.bestOfX);
        ptsDiff = findViewById(R.id.pointsDifference);
        ptsAvl = findViewById(R.id.pointsAvailable);
        switchA = findViewById(R.id.switchA);
        switchB = findViewById(R.id.switchB);
        freeBallSwitch = findViewById(R.id.freeBallSwitch);
        redBall = findViewById(R.id.redBall);
        yellowBall = findViewById(R.id.yellowBall);
        greenBall = findViewById(R.id.greenBall);
        brownBall = findViewById(R.id.brownBall);
        blueBall = findViewById(R.id.blueBall);
        pinkBall = findViewById(R.id.pinkBall);
        blackBall = findViewById(R.id.blackBall);
        lastPointsFouled = false;
        finalRed = false;
        targetBallCurrent = 1;
        enableButtons();

        // Create the Welcome dialog

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        welcomeView = inflater.inflate(R.layout.dialog_welcome, null);
        enterPlayerA = welcomeView.findViewById(R.id.enterPlayerA);
        enterPlayerB = welcomeView.findViewById(R.id.enterPlayerB);
        matchFormat = welcomeView.findViewById(R.id.matchFormat);
        breakView = inflater.inflate(R.layout.dialog_break, null);
        breakA = breakView.findViewById(R.id.breakA);
        breakB = breakView.findViewById(R.id.breakB);
        breakA.setChecked(true);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.matchFormatArray, R.layout.spinner_welcome_style);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_welcome_dropdown_style);
        // Apply the adapter to the spinner
        matchFormat.setAdapter(adapter);

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(welcomeView)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nameAEntered = enterPlayerA.getText().toString().trim();
                        playerAName.setText(nameAEntered);
                        nameBEntered = enterPlayerB.getText().toString().trim();
                        playerBName.setText(nameBEntered);
                        matchFormatChosen = matchFormat.getSelectedItem().toString();
                        matchFormatInt = Integer.parseInt(matchFormatChosen);
                        bestOfX.setText(getResources().getString(R.string.bestofX, matchFormatChosen));
                        breakA.setText(nameAEntered);
                        breakB.setText(nameBEntered);
                        welcomeDialog.dismiss();
                        breakDialog.show();
                    }
                });
        welcomeDialog = builder.create();
        if (savedInstanceState == null) {
            welcomeDialog.show();
        }

        // Create the Select Player to Break dialog
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(breakView)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (breakA.isChecked()) {
                            switchA.setChecked(true);
                            playerAName.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
                            scoreViewA.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
                            playerBName.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
                            scoreViewB.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
                            nextBreaker = nameBEntered;
                        } else {
                            switchB.setChecked(true);
                            playerBName.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
                            scoreViewB.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
                            playerAName.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
                            scoreViewA.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
                            nextBreaker = nameAEntered;
                        }
                        breakDialog.dismiss();
                    }
                });

        breakDialog = builder.create();

        // Create the Reset dialog
        resetView = inflater.inflate(R.layout.dialog_reset, null);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(resetView)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetDialog.dismiss();
                        resetAll();
                        welcomeDialog.show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetDialog.dismiss();
                    }
                });
        resetDialog = builder.create();

        // Create the End Frame dialog
        endFrameView = inflater.inflate(R.layout.dialog_end_frame, null);
        frameSummaryView = inflater.inflate(R.layout.dialog_frame_summary, null);
        matchSummaryView = inflater.inflate(R.layout.dialog_match_summary, null);
        frameSummaryTitle = frameSummaryView.findViewById(R.id.frameSummaryTitle);
        nextBreakerView = frameSummaryView.findViewById(R.id.nextBreakerView);
        frameWinner = frameSummaryView.findViewById(R.id.frameWinner);
        frameScoreSummary = frameSummaryView.findViewById(R.id.frameScoreSummary);
        matchWinner = matchSummaryView.findViewById(R.id.matchWinner);
        matchScoreSummary = matchSummaryView.findViewById(R.id.matchScoreSummary);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(endFrameView);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                updateFrameScore();
                if ((frameScoreA > matchFormatInt / 2) || (frameScoreB > matchFormatInt / 2)) {
                    if (scoreACurrent > scoreBCurrent) {
                        frameWinner.setText(getResources().getString(R.string.frameWinner, nameAEntered));
                        frameScoreSummary.setText(getResources().getString(R.string.frameScoreSummary, scoreACurrent, scoreBCurrent));
                    } else {
                        frameWinner.setText(getResources().getString(R.string.frameWinner, nameBEntered));
                        frameScoreSummary.setText(getResources().getString(R.string.frameScoreSummary, scoreBCurrent, scoreACurrent));
                    }
                    frameSummaryTitle.setText(getResources().getString(R.string.frameSummary, frameNumber));
                    nextBreakerView.setText("");
                    if (frameScoreA > frameScoreB) {
                        matchWinner.setText(getResources().getString(R.string.matchWinner, nameAEntered));
                        matchScoreSummary.setText(getResources().getString(R.string.matchScoreSummary, frameScoreA, frameScoreB));
                    } else {
                        matchWinner.setText(getResources().getString(R.string.matchWinner, nameBEntered));
                        matchScoreSummary.setText(getResources().getString(R.string.matchScoreSummary, frameScoreB, frameScoreA));
                    }
                    endFrameDialog.dismiss();
                    frameSummaryDialog.show();
                } else {
                    if (scoreACurrent > scoreBCurrent) {
                        frameWinner.setText(getResources().getString(R.string.frameWinner, nameAEntered));
                        frameScoreSummary.setText(getResources().getString(R.string.frameScoreSummary, scoreACurrent, scoreBCurrent));
                    } else {
                        frameWinner.setText(getResources().getString(R.string.frameWinner, nameBEntered));
                        frameScoreSummary.setText(getResources().getString(R.string.frameScoreSummary, scoreBCurrent, scoreACurrent));
                    }
                    frameSummaryTitle.setText(getResources().getString(R.string.frameSummary, frameNumber));
                    nextBreakerView.setText(getResources().getString(R.string.nextBreaker, frameNumber + 1, nextBreaker));
                }
                endFrame();
                endFrameDialog.dismiss();
                frameSummaryDialog.show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endFrameDialog.dismiss();
            }
        });
        endFrameDialog = builder.create();

        // Create the Frame Summary dialog
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(frameSummaryView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        frameNumber += 1;
                        frameSummaryDialog.dismiss();
                        if ((frameScoreA > matchFormatInt / 2) || (frameScoreB > matchFormatInt / 2)) {
                            matchSummaryDialog.show();
                        }
                    }
                });
        frameSummaryDialog = builder.create();

        // Create the Match Summary dialog
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(matchSummaryView)
                .setPositiveButton(R.string.newMatch, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        matchSummaryDialog.dismiss();
                        resetAll();
                        welcomeDialog.show();
                    }
                });
        matchSummaryDialog = builder.create();
    }



    //Save the state of the variables and text after the screen orientation changes.

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("scoreAOld", scoreAOld);
        savedInstanceState.putInt("scoreACurrent", scoreACurrent);
        savedInstanceState.putInt("scoreBOld", scoreBOld);
        savedInstanceState.putInt("scoreBCurrent", scoreBCurrent);
        savedInstanceState.putInt("frameScoreA", frameScoreA);
        savedInstanceState.putInt("frameScoreB", frameScoreB);
        savedInstanceState.putInt("pointsDifference", pointsDifference);
        savedInstanceState.putInt("pointsAvailable", pointsAvailable);
        savedInstanceState.putInt("numberOfReds", numberOfReds);
        savedInstanceState.putInt("yellowAvailable", yellowAvailable);
        savedInstanceState.putInt("greenAvailable", greenAvailable);
        savedInstanceState.putInt("brownAvailable", brownAvailable);
        savedInstanceState.putInt("blueAvailable", blueAvailable);
        savedInstanceState.putInt("pinkAvailable", pinkAvailable);
        savedInstanceState.putInt("blackAvailable", blackAvailable);
        savedInstanceState.putBoolean("lastPointsFouled", lastPointsFouled);
        savedInstanceState.putString("playerAName", nameAEntered);
        savedInstanceState.putString("playerBName", nameBEntered);
        savedInstanceState.putString("matchFormat", matchFormatChosen);
        savedInstanceState.putInt("targetBallCurrent", targetBallCurrent);
        savedInstanceState.putInt("targetBallOld", targetBallOld);
        savedInstanceState.putBoolean("finalRed", finalRed);
        savedInstanceState.putInt("potValue", potValue);
        if (redBall.isEnabled()) {
            savedInstanceState.putBoolean("redBallState", true);
        } else {
            savedInstanceState.putBoolean("redBallState", false);
        }
        if (yellowBall.isEnabled()) {
            savedInstanceState.putBoolean("yellowBallState", true);
        } else {
            savedInstanceState.putBoolean("yellowBallState", false);
        }
        if (greenBall.isEnabled()) {
            savedInstanceState.putBoolean("greenBallState", true);
        } else {
            savedInstanceState.putBoolean("greenBallState", false);
        }
        if (brownBall.isEnabled()) {
            savedInstanceState.putBoolean("brownBallState", true);
        } else {
            savedInstanceState.putBoolean("brownBallState", false);
        }
        if (blueBall.isEnabled()) {
            savedInstanceState.putBoolean("blueBallState", true);
        } else {
            savedInstanceState.putBoolean("blueBallState", false);
        }
        if (pinkBall.isEnabled()) {
            savedInstanceState.putBoolean("pinkBallState", true);
        } else {
            savedInstanceState.putBoolean("pinkBallState", false);
        }
        if (blackBall.isEnabled()) {
            savedInstanceState.putBoolean("blackBallState", true);
        } else {
            savedInstanceState.putBoolean("blackBallState", false);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    //Restore the state of the variables and text after the screen orientation changes.

    @Override
    public void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);
        if (saveInstanceState != null) {
            scoreAOld = saveInstanceState.getInt("scoreAOld");
            scoreACurrent = saveInstanceState.getInt("scoreACurrent");
            scoreBOld = saveInstanceState.getInt("scoreBOld");
            scoreBCurrent = saveInstanceState.getInt("scoreBCurrent");
            frameScoreA = saveInstanceState.getInt("frameScoreA");
            frameScoreB = saveInstanceState.getInt("frameScoreB");
            pointsDifference = saveInstanceState.getInt("pointsDifference");
            pointsAvailable = saveInstanceState.getInt("pointsAvailable");
            numberOfReds = saveInstanceState.getInt("numberOfReds");
            yellowAvailable = saveInstanceState.getInt("yellowAvailable");
            greenAvailable = saveInstanceState.getInt("greenAvailable");
            brownAvailable = saveInstanceState.getInt("brownAvailable");
            blueAvailable = saveInstanceState.getInt("blueAvailable");
            pinkAvailable = saveInstanceState.getInt("pinkAvailable");
            blackAvailable = saveInstanceState.getInt("blackAvailable");
            lastPointsFouled = saveInstanceState.getBoolean("lastPointsFouled");
            nameAEntered = saveInstanceState.getString("playerAName");
            nameBEntered = saveInstanceState.getString("playerBName");
            matchFormatChosen = saveInstanceState.getString("matchFormat");
            targetBallCurrent = saveInstanceState.getInt("targetBallCurrent");
            targetBallOld = saveInstanceState.getInt("targetBallOld");
            finalRed = saveInstanceState.getBoolean("finalRed");
            potValue = saveInstanceState.getInt("potValue");
            redBall.setEnabled(saveInstanceState.getBoolean("redBallState"));
            yellowBall.setEnabled(saveInstanceState.getBoolean("yellowBallState"));
            greenBall.setEnabled(saveInstanceState.getBoolean("greenBallState"));
            brownBall.setEnabled(saveInstanceState.getBoolean("brownBallState"));
            blueBall.setEnabled(saveInstanceState.getBoolean("blueBallState"));
            pinkBall.setEnabled(saveInstanceState.getBoolean("pinkBallState"));
            blackBall.setEnabled(saveInstanceState.getBoolean("blackBallState"));

            scoreViewA.setText(String.valueOf(scoreACurrent));
            scoreViewB.setText(String.valueOf(scoreBCurrent));
            frameScoreViewA.setText(String.valueOf(frameScoreA));
            frameScoreViewB.setText(String.valueOf(frameScoreB));
            ptsDiff.setText(String.valueOf("Pts Diff: " + pointsDifference));
            ptsAvl.setText(String.valueOf("Pts Avl: " + pointsAvailable));
            playerAName.setText(String.valueOf(nameAEntered));
            playerBName.setText(String.valueOf(nameBEntered));
            bestOfX.setText("(" + String.valueOf(matchFormatChosen + ")"));

            if (switchA.isChecked()) {
                playerAName.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
                scoreViewA.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
                playerBName.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
                scoreViewB.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
            } else if (switchB.isChecked()) {
                playerAName.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
                scoreViewA.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
                playerBName.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
                scoreViewB.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            }
        }
    }

    /**
     * Switches active player
     */
    public void switchPlayer(View view) {
        if (switchA.isChecked()) {
            playerAName.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            scoreViewA.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            playerBName.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
            scoreViewB.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
            targetBallCurrent = 1;
            freeBallSwitch.setChecked(false);
            enableButtons();
        } else if (switchB.isChecked()) {
            playerAName.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
            scoreViewA.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
            playerBName.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            scoreViewB.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            targetBallCurrent = 1;
            freeBallSwitch.setChecked(false);
            enableButtons();
        }
    }

    /**
     * Displays the score for Player A.
     */
    public void displayForPlayerA(int score) {
        scoreViewA.setText(String.valueOf(score));
    }

    /**
     * Displays the frame score for Player A.
     */
    public void displayFrameForPlayerA(int score) {
        frameScoreViewA.setText(String.valueOf(score));
    }

    /**
     * Displays the score for Player B.
     */
    public void displayForPlayerB(int score) {
        scoreViewB.setText(String.valueOf(score));
    }

    /**
     * Displays the frame score for Player B.
     */
    public void displayFrameForPlayerB(int score) {
        frameScoreViewB.setText(String.valueOf(score));
    }

    /**
     * Enables/Disables the buttons
     */
    public void enableButtons() {
        if (numberOfReds > 0) {
            if (targetBallCurrent == 1) {
                redBall.setEnabled(true);
                yellowBall.setEnabled(false);
                greenBall.setEnabled(false);
                brownBall.setEnabled(false);
                blueBall.setEnabled(false);
                pinkBall.setEnabled(false);
                blackBall.setEnabled(false);
                freeBallSwitch.setEnabled(true);
            } else if (targetBallCurrent == 0) {
                redBall.setEnabled(false);
                yellowBall.setEnabled(true);
                greenBall.setEnabled(true);
                brownBall.setEnabled(true);
                blueBall.setEnabled(true);
                pinkBall.setEnabled(true);
                blackBall.setEnabled(true);
                freeBallSwitch.setEnabled(false);
            }
        } else if (finalRed) {
            redBall.setEnabled(false);
            yellowBall.setEnabled(true);
            greenBall.setEnabled(true);
            brownBall.setEnabled(true);
            blueBall.setEnabled(true);
            pinkBall.setEnabled(true);
            blackBall.setEnabled(true);
            freeBallSwitch.setEnabled(false);
        } else {
            redBall.setEnabled(false);
            yellowBall.setEnabled(false);
            greenBall.setEnabled(false);
            brownBall.setEnabled(false);
            blueBall.setEnabled(false);
            pinkBall.setEnabled(false);
            blackBall.setEnabled(false);
            freeBallSwitch.setEnabled(true);
            if (yellowAvailable == 2) {
                yellowBall.setEnabled(true);
            } else if (greenAvailable == 3) {
                greenBall.setEnabled(true);
            } else if (brownAvailable == 4) {
                brownBall.setEnabled(true);
            } else if (blueAvailable == 5) {
                blueBall.setEnabled(true);
            } else if (pinkAvailable == 6) {
                pinkBall.setEnabled(true);
            } else if (blackAvailable == 7) {
                blackBall.setEnabled(true);
            } else if (scoreACurrent == scoreBCurrent) {
                blackBall.setEnabled(true);
            }
        }
    }

    /**
     * Enables all the buttons when Free Ball switch is turned on
     */
    public void enableAllButtons(View v) {
        if (freeBallSwitch.isChecked()) {
            if (numberOfReds > 0) {
                redBall.setEnabled(true);
            }
            if (yellowAvailable == 2) {
                yellowBall.setEnabled(true);
            }
            if (greenAvailable == 3) {
                greenBall.setEnabled(true);
            }
            if (brownAvailable == 4) {
                brownBall.setEnabled(true);
            }
            if (blueAvailable == 5) {
                blueBall.setEnabled(true);
            }
            if (pinkAvailable == 6) {
                pinkBall.setEnabled(true);
            }
            if (blackAvailable == 7) {
                blackBall.setEnabled(true);
            }
        } else {
            enableButtons();
        }
    }

    /**
     * Adds points for potting a ball
     */
    public void addPoints(int potValue) {
        if (freeBallSwitch.isChecked()) {
            if (switchA.isChecked()) {
                scoreAOld = scoreACurrent;
                scoreACurrent += targetBallCurrent;
                displayForPlayerA(scoreACurrent);
            } else if (switchB.isChecked()) {
                scoreBOld = scoreBCurrent;
                scoreBCurrent += targetBallCurrent;
                displayForPlayerB(scoreBCurrent);
            }
            if (targetBallCurrent == 1) {
                targetBallCurrent = 0;
            }
            freeBallSwitch.setChecked(false);
        } else {
            if (switchA.isChecked()) {
                scoreAOld = scoreACurrent;
                scoreACurrent += potValue;
                displayForPlayerA(scoreACurrent);
            } else if (switchB.isChecked()) {
                scoreBOld = scoreBCurrent;
                scoreBCurrent += potValue;
                displayForPlayerB(scoreBCurrent);
            }
        }
        calcPointsDiff();
        calcPointsAvail();
        lastPointsFouled = false;
        enableButtons();
    }

    /**
     * Adds 1 point for potting a red ball.
     */
    public void potRed(View v) {
        numberOfReds -= 1;
        targetBallOld = targetBallCurrent;
        if (numberOfReds == 0 && !freeBallSwitch.isChecked()) {
            targetBallCurrent = 2;
            finalRed = true;
        } else if (numberOfReds == 0 && freeBallSwitch.isChecked()) {
            targetBallCurrent = 1;
            finalRed = true;
        } else if (freeBallSwitch.isChecked()) {
            targetBallCurrent = 1;
        } else {
            targetBallCurrent = 0;
        }
        potValue = 1;
        addPoints(potValue);
    }

    /**
     * Adds 2 points for potting the yellow ball.
     */
    public void potYellow(View v) {
        targetBallOld = targetBallCurrent;
        if (finalRed) {
            targetBallCurrent = 2;
            finalRed = false;
        } else if ((numberOfReds == 0 && !freeBallSwitch.isChecked()) || (targetBallCurrent == 2)) {
            yellowAvailable = 0;
            targetBallCurrent = 3;
        } else if (freeBallSwitch.isChecked()) {
        } else {
            targetBallCurrent = 1;
        }
        potValue = 2;
        addPoints(potValue);
    }

    /**
     * Adds 3 points for potting the green ball.
     */
    public void potGreen(View v) {
        targetBallOld = targetBallCurrent;
        if (finalRed) {
            targetBallCurrent = 2;
            finalRed = false;
        } else if ((numberOfReds == 0 && !freeBallSwitch.isChecked()) || (targetBallCurrent == 3)) {
            greenAvailable = 0;
            targetBallCurrent = 4;
        } else if (freeBallSwitch.isChecked()) {
        } else {
            targetBallCurrent = 1;
        }
        potValue = 3;
        addPoints(potValue);
    }

    /**
     * Adds 4 points for potting the brown ball.
     */
    public void potBrown(View v) {
        targetBallOld = targetBallCurrent;
        if (finalRed) {
            targetBallCurrent = 2;
            finalRed = false;
        } else if ((numberOfReds == 0 && !freeBallSwitch.isChecked()) || (targetBallCurrent == 4)) {
            brownAvailable = 0;
            targetBallCurrent = 5;
        } else if (freeBallSwitch.isChecked()) {
        } else {
            targetBallCurrent = 1;
        }
        potValue = 4;
        addPoints(potValue);
    }

    /**
     * Adds 5 points for potting the blue ball.
     */
    public void potBlue(View v) {
        targetBallOld = targetBallCurrent;
        if (finalRed) {
            targetBallCurrent = 2;
            finalRed = false;
        } else if ((numberOfReds == 0 && !freeBallSwitch.isChecked()) || (targetBallCurrent == 5)) {
            blueAvailable = 0;
            targetBallCurrent = 6;
        } else if (freeBallSwitch.isChecked()) {
        } else {
            targetBallCurrent = 1;
        }
        potValue = 5;
        addPoints(potValue);
    }

    /**
     * Adds 6 points for potting the pink ball.
     */
    public void potPink(View v) {
        targetBallOld = targetBallCurrent;
        if (finalRed) {
            targetBallCurrent = 2;
            finalRed = false;
        } else if ((numberOfReds == 0 && !freeBallSwitch.isChecked()) || (targetBallCurrent == 6)) {
            pinkAvailable = 0;
            targetBallCurrent = 7;
        } else if (freeBallSwitch.isChecked()) {
        } else {
            targetBallCurrent = 1;
        }
        potValue = 6;
        addPoints(potValue);
    }

    /**
     * Adds 7 points for potting the black ball.
     */
    public void potBlack(View v) {
        targetBallOld = targetBallCurrent;
        if (finalRed) {
            targetBallCurrent = 2;
            finalRed = false;
        } else if ((numberOfReds == 0 && !freeBallSwitch.isChecked()) || (targetBallCurrent == 6)) {
            blackAvailable = 0;
            targetBallCurrent = 7;
        } else if (freeBallSwitch.isChecked()) {
        } else {
            targetBallCurrent = 1;
        }
        potValue = 7;
        addPoints(potValue);
    }

    /**
     * Adds points to the opponent for a foul
     */
    public void addFoul(int foulValue) {
        if (switchA.isChecked()) {
            scoreBOld = scoreBCurrent;
            scoreBCurrent = scoreBCurrent + foulValue;
            displayForPlayerB(scoreBCurrent);
        } else if (switchB.isChecked()) {
            scoreAOld = scoreACurrent;
            scoreACurrent = scoreACurrent + foulValue;
            displayForPlayerA(scoreACurrent);
        }
        calcPointsDiff();
        calcPointsAvail();
        lastPointsFouled = true;
    }

    /**
     * Adds 4 points to the opponent for a foul
     */
    public void foul4(View view) {
        foulValue = 4;
        addFoul(foulValue);
    }

    /**
     * Adds 5 points to the opponent for a foul
     */
    public void foul5(View view) {
        foulValue = 5;
        addFoul(foulValue);
    }

    /**
     * Adds 6 points to the opponent for a foul
     */
    public void foul6(View view) {
        foulValue = 6;
        addFoul(foulValue);
    }

    /**
     * Adds 7 points to the opponent for a foul
     */
    public void foul7(View view) {
        foulValue = 7;
        addFoul(foulValue);
    }

    /**
     * Undoes the last points added
     */
    public void undo(View v) {
        if (((switchA.isChecked() && !lastPointsFouled)) || (switchB.isChecked() && lastPointsFouled)) {
            if (scoreACurrent == scoreAOld + 1) {
                numberOfReds = numberOfReds + 1;
            }
            if ((scoreACurrent == scoreAOld + 2) && (yellowAvailable == 0)) {
                yellowAvailable = 2;
            }
            if ((scoreACurrent == scoreAOld + 3) && (greenAvailable == 0)) {
                greenAvailable = 3;
            }
            if ((scoreACurrent == scoreAOld + 4) && (brownAvailable == 0)) {
                brownAvailable = 4;
            }
            if ((scoreACurrent == scoreAOld + 5) && (blueAvailable == 0)) {
                blueAvailable = 5;
            }
            if ((scoreACurrent == scoreAOld + 6) && (pinkAvailable == 0)) {
                pinkAvailable = 6;
            }
            if ((scoreACurrent == scoreAOld + 7) && (blackAvailable == 0)) {
                blackAvailable = 7;
            }
            scoreACurrent = scoreAOld;
            displayForPlayerA(scoreACurrent);
        } else if (((switchB.isChecked() && !lastPointsFouled)) || (switchA.isChecked() && lastPointsFouled)) {
            if (scoreBCurrent == scoreBOld + 1) {
                numberOfReds = numberOfReds + 1;
            }
            if ((scoreBCurrent == scoreBOld + 2) && (yellowAvailable == 0)) {
                yellowAvailable = 2;
            }
            if ((scoreBCurrent == scoreBOld + 3) && (greenAvailable == 0)) {
                greenAvailable = 3;
            }
            if ((scoreBCurrent == scoreBOld + 4) && (brownAvailable == 0)) {
                brownAvailable = 4;
            }
            if ((scoreBCurrent == scoreBOld + 5) && (blueAvailable == 0)) {
                blueAvailable = 5;
            }
            if ((scoreBCurrent == scoreBOld + 6) && (pinkAvailable == 0)) {
                pinkAvailable = 6;
            }
            if ((scoreBCurrent == scoreBOld + 7) && (blackAvailable == 0)) {
                blackAvailable = 7;
            }
            scoreBCurrent = scoreBOld;
            displayForPlayerB(scoreBCurrent);
        }
        targetBallCurrent = targetBallOld;
        calcPointsDiff();
        calcPointsAvail();
        enableButtons();
    }

    /**
     * Displays the points difference.
     */
    public void displayPointsDifference(int pointsDiff) {
        ptsDiff.setText(String.valueOf(getResources().getString(R.string.pntsDiffBlank) + pointsDiff));
    }

    /**
     * Calculates the points difference.
     */
    public void calcPointsDiff() {
        if (scoreACurrent > scoreBCurrent) {
            pointsDifference = scoreACurrent - scoreBCurrent;
            displayPointsDifference(pointsDifference);
        } else if (scoreBCurrent > scoreACurrent) {
            pointsDifference = scoreBCurrent - scoreACurrent;
            displayPointsDifference(pointsDifference);
        } else {
            pointsDifference = 0;
            displayPointsDifference(pointsDifference);
        }
    }

    /**
     * Displays the points available.
     */
    public void displayPointsAvailable(int pointsAvail) {
        ptsAvl.setText(String.valueOf(getResources().getString(R.string.pntsAvlBlank) + pointsAvail));
    }

    /**
     * Calculates the points available.
     */
    public void calcPointsAvail() {
        pointsAvailable = (numberOfReds * 8) + yellowAvailable + greenAvailable + brownAvailable + blueAvailable + pinkAvailable + blackAvailable;
        displayPointsAvailable(pointsAvailable);
    }

    /**
     * Calculates if any reds are remaining and removes colours if necessary.
     */
    public void removeColours() {
        if (numberOfReds > 0) {
            ;
        } else if (yellowAvailable > 0) {
            yellowAvailable = 0;
        } else if (greenAvailable > 0) {
            greenAvailable = 0;
        } else if (brownAvailable > 0) {
            brownAvailable = 0;
        } else if (blueAvailable > 0) {
            blueAvailable = 0;
        } else if (pinkAvailable > 0) {
            pinkAvailable = 0;
        } else if (blackAvailable > 0) {
            blackAvailable = 0;
        }
    }

    /**
     * Resets all variables apart from frame score to initial values
     */
    public void resetVariables() {
        scoreAOld = 0;
        scoreACurrent = 0;
        scoreBOld = 0;
        scoreBCurrent = 0;
        pointsDifference = 0;
        pointsAvailable = 147;
        numberOfReds = 15;
        yellowAvailable = 2;
        greenAvailable = 3;
        brownAvailable = 4;
        blueAvailable = 5;
        pinkAvailable = 6;
        blackAvailable = 7;
        enterPlayerA.setText("");
        enterPlayerB.setText("");
        matchFormat.setSelection(0);
        targetBallCurrent = 1;
        enableButtons();
    }

    /**
     * Resets everything
     */
    public void resetAll() {
        resetVariables();
        frameScoreA = 0;
        frameScoreB = 0;
        frameNumber = 1;
        displayForPlayerA(scoreACurrent);
        displayForPlayerB(scoreBCurrent);
        displayFrameForPlayerA(frameScoreA);
        displayFrameForPlayerB(frameScoreB);
        displayPointsAvailable(pointsAvailable);
        displayPointsDifference(pointsDifference);
    }

    /**
     * Opens the End Frame dialog
     */
    public void openEndFrameDialog(View view) {
        if (scoreACurrent != scoreBCurrent) {
            endFrameDialog.show();
        } else {
            Toast scoresLevel = Toast.makeText(getBaseContext(), "Scores are level", Toast.LENGTH_LONG);
            scoresLevel.show();
        }
    }

    /**
     * Updates frame score
     */
    public void updateFrameScore() {
        if (scoreACurrent > scoreBCurrent) {
            frameScoreA += 1;
            displayFrameForPlayerA(frameScoreA);
        } else if (scoreBCurrent > scoreACurrent) {
            frameScoreB += 1;
            displayFrameForPlayerB(frameScoreB);
        }
    }

    /**
     * Ends the frame and resets all variables.
     */
    public void endFrame() {
        resetVariables();
        displayForPlayerA(scoreACurrent);
        displayForPlayerB(scoreBCurrent);
        displayPointsAvailable(pointsAvailable);
        displayPointsDifference(pointsDifference);
        if (nextBreaker == nameAEntered) {
            nextBreaker = nameBEntered;
            switchA.setChecked(true);
            playerAName.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            scoreViewA.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            playerBName.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
            scoreViewB.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
        } else {
            nextBreaker = nameAEntered;
            switchB.setChecked(true);
            playerBName.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            scoreViewB.setBackground(getResources().getDrawable(R.drawable.activeplayerbackground));
            playerAName.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
            scoreViewA.setBackground(getResources().getDrawable(R.drawable.inactiveplayerbackground));
        }
    }

    /**
     * Opens the Reset Dialog
     */
    public void openResetDialog(View view) {
        resetDialog.show();
    }

}
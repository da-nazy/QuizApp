package app.datableed.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;



public class MainActivity extends AppCompatActivity {
    // for pauseDialog
    TextView resume;
    timerViewModel mtimerModel;
    // for savedInstacance 1d array
    ArrayList<String>savedInstance_quiz1d=new ArrayList<>();
    ArrayList<ArrayList<String>> savedInstance_quiz2d=new ArrayList<>();
    // Values for saved instance state to avoid application loosing data when the app has rotated or....
    // This process is done on the onSavedInstance State
    private static final String KEY_SCORE="keyScore";
    private static final String KEY_QUESTION_COUNT="keyQuestionCount";
    private static final String KEY_MILLIS_LEFT="keyMillisLeft";
   // private static final String KEY_ANSWERED="keyAnswered";
    private static  final String KEY_QUESTION_LIST ="keyQuestionList";
    // this identifies the question Label
    private static  final String KEY_QUESTION="KeyQuestion";
    // this identifies the questions answers
    private static  final  String Key_QUESTION1="KeyQuestion1";
    private static  final String Key_QUESTION2="KeyQuestion2";
    private static final  String Key_QUESTION3="KeyQuestion3";
    private static final  String Key_QUESTION4="KeyQuestion4";
    private static final String  KEY_RIGHTANSWER="keyRightAnswer";

    // made quiz a global variable to be able to use it in the parcelable arrayList;
    ArrayList<String> quiz;
    // quizover determins if the quize has finished
    //Boolean quizOver=false;
     Bundle saved;
    Button qbtn1, qbtn2, qbtn3, qbtn4,rightAnswerBtn;
    LinearLayout timerBg;
    TextView questionCountLable, questionLabel, score, countdown, timerTxt,tv1,tv2,tv3,tv4,bonus;
    Animation abtn1, abtn2, abtn3, abtn4, ibf,textUp,textFade,scaleUp,scaleDown;
    ImageButton cb1, cb2, cb3, cb4, wb1, wb2, wb3, wb4,pauseButton;
    public String rightAnswer;
    public int rightAnswerCount = 0;
    View mview;
    // Setting up countdown timer
     public static   final long COUNTDOWN_IN_MILIS =11000;
    private ColorStateList textColorDefaultcd;

    private CountDownTimer countDownTimer;
    public   transient static long timeLeftInMills=COUNTDOWN_IN_MILIS;
    boolean timerRunning=true;


    int quizCount = 1;
    Button answerBtn;
    Button rightAnswerCheckbtn;
    // Right answer should run check
    // to avoid uninitialized variables
    boolean rightans;

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    ArrayList<questionModel> question = new ArrayList<>();
    // This boolean checks if button animation is done
    boolean isBAD = false;

    // switch for music and sound
    SwitchCompat music_switch,sound_switch;
    boolean sound_check;
    boolean sound_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Starting the background music
        backgroundMusic.runMusic(backgroundMusic.state,this);

        // binding views to the respect widgets
        bindView();

        countdown();
        //mtimerModel=new ViewModelProvider(this).get(timerViewModel.class);
        mtimerModel=new ViewModelProvider(this).get(timerViewModel.class);
        //saved=savedInstanceState;
        // Setting the pauseButton onClick Listener
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"pause Button clicked",Toast.LENGTH_SHORT).show();
                // 1 When clicked the timer is paused
                // 2 Dialog window is then displayed
                // 3 implement the functions in the dialog window
                pauseDialog();
            }
        });


        question.add(new questionModel("What is my name", "Daniel", "Adamu", "chuky", "favour"));
        question.add(new questionModel("What is your name", "chuky", "Adamu", "ben", "favour"));
        question.add(new questionModel("What is our name", "Adamu", "great", "chuky", "favour"));
        question.add(new questionModel("What is name", "favour", "Adamu", "chuky", "gozia"));

        for (int i = 0; i < question.size(); i++) {

            // Prepare array
            ArrayList<String> tempArray = new ArrayList<>();
            tempArray.add(question.get(i).getQuestionLabel());
            tempArray.add(question.get(i).getRightAnswer());
            tempArray.add(question.get(i).getAnswer1());
            tempArray.add(question.get(i).getAnswer2());
            tempArray.add(question.get(i).getAnswer3());

            // Add tempArray to QuizArray
            quizArray.add(tempArray);


        }


        showNextQuiz();
           if(savedInstanceState!=null){

               ArrayList<String> temp=new ArrayList<>();
               ArrayList<ArrayList<String>> temp2d=new ArrayList<>();

               // get the 1d quiz array and store in 2d quiz array
               // Question_text
               questionLabel.setText(savedInstanceState.getString(KEY_QUESTION));
               // Question answers
               qbtn1.setText(savedInstanceState.getString(Key_QUESTION1));
               qbtn2.setText(savedInstanceState.getString(Key_QUESTION2));
               qbtn3.setText(savedInstanceState.getString(Key_QUESTION3));
               qbtn4.setText(savedInstanceState.getString(Key_QUESTION4));
             // Toast.makeText(getApplicationContext(),""+savedInstanceState.getLong(KEY_MILLIS_LEFT), Toast.LENGTH_SHORT).show();
           //   countDownTimer.onTick(savedInstanceState.getLong(KEY_MILLIS_LEFT));
              // timeLeftInMills=savedInstanceState.getLong(KEY_MILLIS_LEFT);
               score.setText(savedInstanceState.getInt(KEY_SCORE)+"");
               // quizCount=saved.getInt(KEY_SCORE);
               rightAnswer=""+savedInstanceState.getString(KEY_RIGHTANSWER);
               // The program below converts 1d array to 2d array and stores in the quizarray
               temp=savedInstanceState.getStringArrayList(KEY_QUESTION_LIST);
               ArrayList<String> temp2=new ArrayList<>();
               for(int a=0; a<temp.size();a++){
                   temp2.add(temp.get(a));
                   if(temp2.size()==5){
                       String good[]=new String[temp2.size()];
                       for(int m=0; m<good.length;m++){
                           good[m]=temp2.get(m);
                       }
                       temp2d.add(new ArrayList<String>(Arrays.asList(good)));
                       temp2.clear();
                   }
               }

               if(temp2d.size()!=0){
                   quizArray.clear();
                   quizArray=temp2d;
               }

               quizCount=savedInstanceState.getInt(KEY_QUESTION_COUNT);
               questionCountLable.setText(quizCount+"/"+question.size());

           }






        // handling button clicks
        qbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  textMoveFade(tv1,v);
               checkAnswer(v);


            }
        });
        qbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textMoveFade(tv2,v);
                checkAnswer(v);


            }
        });
        qbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // textMoveFade has checkAnswer method in it
                // after the text bonus has been awarded then the checkanswer is called
                // if bonux isn't awarded then check answer is still called
               // textMoveFade(tv3,v);
                checkAnswer(v);


            }
        });
        qbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // textMoveFade(tv4,v);
                checkAnswer(v);

            }
        });

    }

    public void showNextQuiz() {



                // update quizCountLabel.
                questionCountLable.setText(quizCount + "/" + question.size());

                // Generate random number between 0 and 14 (QuizArray's size -1)
                Random random = new Random();
                int randomNum = random.nextInt(quizArray.size());

                // pick one quiz set
                //  ArrayList<String> quiz = quizArray.get(randomNum);
                quiz=quizArray.get(randomNum);

                // set question and right answer
                questionLabel.setText(quiz.get(0));
                rightAnswer = quiz.get(1);
                // int a[]=new int[2];
                // how to shuffle an array list

                // Remove "Country" from quiz and shuffle choices
                // This should have removed the question from the list and shuffle it.
                quiz.remove(0);
                Collections.shuffle(quiz);

                // set Choices
                qbtn1.setText(quiz.get(0));
                qbtn2.setText(quiz.get(1));
                qbtn3.setText(quiz.get(2));
                qbtn4.setText(quiz.get(3));


                // Remove this quiz from quizArray
                quizArray.remove(randomNum);




    }

    public void bindView() {
        qbtn1 = findViewById(R.id.qbtn1);
        qbtn2 = findViewById(R.id.qbtn2);
        qbtn3 = findViewById(R.id.qbtn3);
        qbtn4 = findViewById(R.id.qbtn4);
        questionCountLable = findViewById(R.id.countLable);
        questionLabel = findViewById(R.id.textView);
        score = findViewById(R.id.score);
        // Imagebuttons correct
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        //Wrong image buttons
        wb1 = findViewById(R.id.wb1);
        wb2 = findViewById(R.id.wb2);
        wb3 = findViewById(R.id.wb3);
        wb4 = findViewById(R.id.wb4);
        // for countdown timer
        countdown = findViewById(R.id.countDown);
        timerBg = findViewById(R.id.timerBd);
        timerTxt = findViewById(R.id.timerTxt);
        // for textView Animation
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        tv4=findViewById(R.id.tv4);
        // for bonus animation
        bonus=findViewById(R.id.bonus);
        pauseButton=findViewById(R.id.pauseButton);


    }

    public void loadAnim() {

        abtn1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        abtn2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2);
        abtn3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide3);
        abtn4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide4);

        qbtn1.startAnimation(abtn1);
        qbtn2.startAnimation(abtn2);
        qbtn3.startAnimation(abtn3);
        qbtn4.startAnimation(abtn4);

    }


    public void countdown() {
        textColorDefaultcd = countdown.getTextColors();
        // show next where we want to start our countdown
           //COUNTDOWN_IN_MILIS=11000;
//       mtimerModel.setTiemLeftInMilis(COUNTDOWN_IN_MILIS);
       timeLeftInMills =COUNTDOWN_IN_MILIS;
        timerRunning=true;
        startCountDown();

    }

    public void startCountDown() {

        countDownTimer = new   CountDownTimer(timeLeftInMills, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
              //  millisUntilFinished=COUNTDOWN_IN_MILIS+System.currentTimeMillis();
                timeLeftInMills = millisUntilFinished;

                //Toast.makeText(getApplicationContext(),+"",Toast.LENGTH_SHORT).show();
                // This checks when the time is 5 seconds and starts blinking

                if((timeLeftInMills/1000)%60<=5) {
                    blink(true);
                } else if((timeLeftInMills/1000)%60>5){
                    blink(false);
                }

                updateCountDownText();

            }

            @Override
            public void onFinish() {
               timeLeftInMills = 0;
                updateCountDownText();
               // Toast.makeText(getApplicationContext(),"The counter has finished",Toast.LENGTH_SHORT).show();
                timeUpDialog();
             //  MainActivity.super.onSaveInstanceState();
               timerRunning=false;


            }
        }.start();
    }

    public void updateCountDownText() {
        //  int minutes=(int) (timeLeftInMills/1000)/60;
        int seconds = (int) (timeLeftInMills / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), ":%02d", seconds);

        countdown.setText(timeFormatted);

        if (timeLeftInMills < 1000) {
            countdown.setTextColor(Color.RED);
        } else {
            countdown.setTextColor(textColorDefaultcd);
        }

    }

    public void checkAnswer( View v) {
        // Get pushed button
        countDownTimer.cancel();
        // countDownTimer should be called again after it has been cancled.
        answerBtn = findViewById(v.getId());
        String btnText = answerBtn.getText().toString();
        String alertTitle;
        if (btnText.equals(rightAnswer)) {
            switch (v.getId()) {
                case R.id.qbtn1://cb1.setVisibility(View.VISIBLE);
                    playSound(R.raw.wright_sound);
                    textMoveFade(tv1,qbtn1,cb1);
                    //rightAnserChecked(qbtn1,cb1);
                   // qbtn1.setBackgroundResource(R.mipmap.corectrect);
                   // qbtn1.setTextColor(Color.WHITE);
                    //animateButton(cb1);
                    break;
                case R.id.qbtn2://cb2.setVisibility(View.VISIBLE);
                    playSound(R.raw.wright_sound);
                    textMoveFade(tv2,qbtn2,cb2);
                    //rightAnserChecked(qbtn2,cb2);
                    //qbtn2.setBackgroundResource(R.mipmap.corectrect);
                    //qbtn2.setTextColor(Color.WHITE);
                   // animateButton(cb2);
                    break;
                case R.id.qbtn3://cb3.setVisibility(View.VISIBLE);
                    playSound(R.raw.wright_sound);
                    textMoveFade(tv3,qbtn3,cb3);
                    //rightAnserChecked(qbtn3,cb3);
                    //qbtn3.setBackgroundResource(R.mipmap.corectrect);
                   // qbtn3.setTextColor(Color.WHITE);
                    //animateButton(cb3);
                    break;
                case R.id.qbtn4://cb4.setVisibility(View.VISIBLE);
                    playSound(R.raw.wright_sound);
                    textMoveFade(tv4,qbtn4,cb4);
                    //rightAnserChecked(qbtn4,cb4);
                   // qbtn4.setBackgroundResource(R.mipmap.corectrect);
                  //  qbtn4.setTextColor(Color.WHITE);
                  //  animateButton(cb4);
                    break;
            }

            //   answerBtn.setBackgraoundResource(R.mipmap.corectrect);
            // answerBtn.setTextColor(Color.WHITE);
            // correct!
            alertTitle = "Correct";
            // Should include score animation right here

            rightAnswerCount+=5;
            //score.setText(Integer.parseInt(score.getText().toString()) + rightAnswerCount + "");
            scoreAnimation(rightAnswerCount);
        } else {
            // aim to identify the right answer even when the wrong one has been clicked
            rightans = true;
            rightAnswerCheckbtn = findViewById(rightAnserButtonCheck());
            rightAnswerCheckbtn.setBackgroundResource(R.mipmap.corectrect);
            rightAnswerCheckbtn.setTextColor(Color.WHITE);
            // wrong
            alertTitle = "wrong";
            switch (v.getId()) {
                case R.id.qbtn1://wb1.setVisibility(View.VISIBLE);
                    playSound(R.raw.wrong_sound);
                    animateButton(wb1);
                    break;
                case R.id.qbtn2://wb2.setVisibility(View.VISIBLE);
                    playSound(R.raw.wrong_sound);
                    animateButton(wb2);
                    break;
                case R.id.qbtn3://wb3.setVisibility(View.VISIBLE);
                    playSound(R.raw.wrong_sound);
                    animateButton(wb3);
                    break;
                case R.id.qbtn4://wb4.setVisibility(View.VISIBLE);
                    playSound(R.raw.wrong_sound);
                    animateButton(wb4);
                    break;
            }
        }

        if (quizCount == question.size()) {
          //  quizOver=true;
            // Show result
            //answerBtn.setBackgroundResource(R.mipmap.rectangle1);
            //answerBtn.setTextColor(Color.BLACK);

            //removeButton(answerBtn.getId());
            // this  where I either change the activity because the user has finished answering the questions
        } else {
            quizCount++;
            // answerBtn.setBackgroundResource(R.mipmap.rectangle1);
            //answerBtn.setTextColor(Color.BLACK);
            // This is to be able to delay to meet up
            // removeButton(answerBtn.getId());


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    answerBtn.setBackgroundResource(R.mipmap.rectangle1);
                    answerBtn.setTextColor(Color.BLACK);

                    // resetting the right answer button
                    if (rightans == true) {
                        // This check helps to avoid running into error caused when a button clicked is true theirby making the right answer button not to be called or initialized
                        rightAnswerCheckbtn.setBackgroundResource(R.mipmap.rectangle1);
                        rightAnswerCheckbtn.setTextColor(Color.BLACK);
                        rightans = false;
                    }

                    //Do something after 100ms
                    showNextQuiz();
                }
            }, 1000);
           // timerRunning=false;
            countDownTimer.start();

        }

        /**
         *   // Create Dialog
         *         AlertDialog.Builder builder= new AlertDialog.Builder(this);
         *         builder.setTitle(alertTitle);
         *         builder.setMessage("Answer:"+rightAnswer);
         *         builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
         *             @Override
         *             public void onClick(DialogInterface dialog, int which) {
         *                 if(quizCount==question.size()){
         *                     // Show result
         *                     answerBtn.setBackgroundResource(R.mipmap.rectangle1);
         *                     answerBtn.setTextColor(Color.BLACK);
         *
         *                  //removeButton(answerBtn.getId());
         *                 }else{
         *                     quizCount++;
         *                     answerBtn.setBackgroundResource(R.mipmap.rectangle1);
         *                     answerBtn.setTextColor(Color.BLACK);
         *
         *                    // removeButton(answerBtn.getId());
         *
         *                     showNextQuiz();
         *
         *                 }
         *             }
         *         });
         *         builder.setCancelable(false);
         *         builder.show();
         */
    }

    public void removeButton(int id) {
        switch (id) {
            case R.id.qbtn1:
                cb1.setVisibility(View.GONE);
                wb1.setVisibility(View.GONE);
                break;
            case R.id.qbtn2:
                cb2.setVisibility(View.GONE);
                wb2.setVisibility(View.GONE);
                break;
            case R.id.qbtn3:
                cb3.setVisibility(View.GONE);
                wb3.setVisibility(View.GONE);
                break;
            case R.id.qbtn4:
                cb4.setVisibility(View.GONE);
                wb4.setVisibility(View.GONE);
                break;
        }


    }

    public void  rightAnserChecked(Button btn,ImageButton ibtn){
        btn.setBackgroundResource(R.mipmap.corectrect);
        btn.setTextColor(Color.WHITE);
        animateButton(ibtn);
    }

    // if loadbuton is done show next question

    public void animateButton(View v) {
        // This is the right answer button view
        // and load animation was done after right answer was indicated
        // button background should also be cleared to the right one
        mview = v;
        mview.setVisibility(View.VISIBLE);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(200);

        mview.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mview.setVisibility(View.GONE);
                answerBtn.setBackgroundResource(R.mipmap.rectangle1);
                answerBtn.setTextColor(Color.BLACK);
                /**
                 * final Handler handler=new Handler();
                 *         handler.postDelayed(new Runnable() {
                 *             @Override
                 *             public void run() {
                 *                 alertDialog.dismiss();
                 *                 timeUpContinue();
                 *             }
                 *         },3500);
                 */
                final Handler handler =new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadAnim();
                    }
                },900);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public int rightAnserButtonCheck() {
        // first we have an array of buttons that hold the answers
        // Loop through the button array
        // Create a button object to get the answer and compare with the right one
        // Get the id of the right answer when gotten and store .
        // return the id of the right answer
        // check if right answer is not 0

        Button answer;
        int rightanswer = 0;
        int[] checkanswer = {R.id.qbtn1, R.id.qbtn2, R.id.qbtn3, R.id.qbtn4};

        for (int a = 0; a < checkanswer.length; a++) {

            answer = findViewById(checkanswer[a]);

            if (answer.getText().equals(rightAnswer)) {
                rightanswer= checkanswer[a];
            }
        }

        return rightanswer;
    }
    // What is left
    // 1 Timer
    // 2 Bonus point scale
    // 3 Bonus point score fade out

    public void blink(Boolean shouldRun) {
        // aim is to change the backgroundcolor of the timer at intervals
        // 1 change the background
        // 2 change the background text to white
        // 3
        //AnimatorSet anim=new AnimatorSet();
        //anim.playSequentially(ObjectAnimator.ofInt(timerBg,"backgroundColor",Color.RED,Color.WHITE),
        //      ObjectAnimator.ofInt(timerTxt,"textColor",Color.WHITE,Color.BLACK)
        //     );
        textblink(shouldRun);
        countDownblink(shouldRun);
        bgblink(shouldRun);

    }

    public void textblink(Boolean shouldRun) {
    // to use true and false  to indicate the start and stop of an animation : shouldRun variable provided
        ObjectAnimator anim = ObjectAnimator.ofInt(timerTxt, "textColor", Color.WHITE, Color.BLACK);
        if(shouldRun==true){
            anim.setDuration(1000);
           anim.setStartDelay(10);
            anim.setEvaluator(new ArgbEvaluator());
            //anim.setRepeatMode(Animation.REVERSE);
        //    anim.setRepeatCount(Animation.INFINITE);
            anim.start();
        }
        else{

         //  anim.setDuration(0);
        }



    }

    public void bgblink(Boolean shouldRun) {
        // to use true and false  to indicate the start and stop of an animation : shouldRun variable provided
        ObjectAnimator anim = ObjectAnimator.ofInt(timerBg, "backgroundColor", Color.RED, Color.WHITE);
        if(shouldRun==true){
            anim.setDuration(1000);
            anim.setStartDelay(10);
            anim.setEvaluator(new ArgbEvaluator());
            //anim.setRepeatMode(Animation.REVERSE);
          //  anim.setRepeatCount(Animation.INFINITE);
            anim.start();
        }else{

           ///anim.setDuration(0);
        }

    }

    public void countDownblink(Boolean shouldRun) {
        // to use true and false  to indicate the start and stop of an animation : shouldRun variable provided
        ObjectAnimator anim = ObjectAnimator.ofInt(countdown, "textColor", Color.WHITE, Color.RED);

        if(shouldRun==true){
            anim.setDuration(1000);
            anim.setStartDelay(10);
            anim.setEvaluator(new ArgbEvaluator());
            //anim.setRepeatMode(Animation.REVERSE);
           // anim.setRepeatCount(Animation.INFINITE);
            anim.start();
        }else{
         // anim.setDuration(0);

        }

    }
    public void textMoveFade(TextView tv,Button btn,ImageButton ibtn){
        // The aim and objective of this function
           //1 if the use answered and time is timer >=9 then a bonus point of 200 is given
           // 2 if answered and timer is timer>=7 && timer<9 then a bonus point of 100 is given
           // if answered and timer is <=5 then no point should be given the the checkanswered method is meant to run smoothly
           int time=(int) (timeLeftInMills / 1000) % 60;
           if(time>=8){
               // should give 200 bonus point
               bonusAnimation();
               textMoveFadeAnimation(tv,200,btn,ibtn);
           }else if(time>=7 && time<=8){
               // should be given 100 bonus point
               bonusAnimation();
              textMoveFadeAnimation(tv,100,btn,ibtn);
           }else {
               // Timer is <=5 and nothing should happen
               rightAnserChecked(btn,ibtn);

           }



       }
       public void textMoveFadeAnimation(TextView tv,int bonus,Button btn,ImageButton ibtn){

           final View m=tv;
           final Button but=btn;
           final ImageButton imagebtn=ibtn;

           TextView mv=(TextView) tv;
            // Todo this bonus mark needs to be added to the current score
           switch(bonus){
               case 200:mv.setText("200");
                   rightAnswerCount+=200;
                   break;
               case 100:mv.setText("100");
                    rightAnswerCount+=100;
                   break;
               default:System.out.println("Error: Input not defined");
                   break;
           }
           m.setVisibility(View.VISIBLE);
           textFade=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.textfadeout);
           textUp=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.textmoveup);
           AnimationSet s = new AnimationSet(false);
           s.addAnimation(textUp);
           s.addAnimation(textFade);
           m.startAnimation(s);
           s.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {

               }

               @Override
               public void onAnimationEnd(Animation animation) {
                   m.setVisibility(View.GONE);
                  rightAnserChecked(but,imagebtn);
               }

               @Override
               public void onAnimationRepeat(Animation animation) {

               }
           });
    }
    public void pauseDialog(){
        // To implement all the pause functions below
        // 1  Resume quize
        // 2  Restart Quize
        // 3 exit Quize
        // 4 Turn sound on or off
     // for countdown time left
        // saving time and pausing time
        final long timeleft=timeLeftInMills;
        countDownTimer.cancel();
        ViewGroup  viewGroup=findViewById(R.id.content);
        final View pauseDialog= LayoutInflater.from(this).inflate(R.layout.pause_dialog,viewGroup,false);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(pauseDialog);
        final AlertDialog alertDialog=builder.create();
        resume=pauseDialog.findViewById(R.id.resumeQuiz);
        // Bind the view for switch here or else you did be getting a null pointer exception
        // for switch buttons of music
        music_switch=pauseDialog.findViewById(R.id.music_switch);
        sound_switch=pauseDialog.findViewById(R.id.sound_switch);

        sound_switch.setChecked(sound_state);
         music_switch.setChecked(backgroundMusic.state);

        // switch for right or wrong answers
        sound_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sound_state=true;
                    sound_check=sound_state;
                    buttonView.setChecked(sound_state);
                }else{
                    sound_state=false;
                    sound_check=sound_state;
                    buttonView.setChecked(sound_state);
                }
            }
        });

        // swtich for background music
        music_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    backgroundMusic.state=true;
                    backgroundMusic.resume();
                    buttonView.setChecked(backgroundMusic.state);
                }else{
                    backgroundMusic.state=false;
                    backgroundMusic.pause();
                    buttonView.setChecked(backgroundMusic.state);
                }
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"Clicked on Resume",Toast.LENGTH_SHORT).show();
                // dismiss the alert dialog so that the onDismissed listerner will continue from there
                alertDialog.dismiss();


            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        try {
            alertDialog.show();
            alertDialog.setCancelable(true);

        }catch(Exception e){
            e.printStackTrace();
        }
    /*
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    alertDialog.dismiss();
                    // timeLeftInMills=11000;
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        },3500);
     */

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // restarting the puased time
                //timeLeftInMills=timeleft;
                //countDownTimer.onTick(timeleft);

                startCountDown();

               // timeUpContinue();
            }
        });
    }

    public void timeUpDialog(){
        ViewGroup  viewGroup=findViewById(R.id.content);
        View sorryDialog= LayoutInflater.from(this).inflate(R.layout.activity_times_up,viewGroup,false);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setView(sorryDialog);



        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        try {
            alertDialog.show();
        }catch(Exception e){
            e.printStackTrace();
        }
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              try {
                  alertDialog.dismiss();
                 // timeLeftInMills=11000;
              }catch(Exception e){
                  e.printStackTrace();
              }

            }
        },3500);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                timeUpContinue();
            }
        });
    }


    public void timeUpContinue(){
        // To select the right answer
        // change the color from green to white
        // then load swipe animation
        //  and change questions
       // rightAnserButtonCheck(); returns the id of right answer which is int datatype
      rightAnswerBtn=findViewById(rightAnserButtonCheck());
      // ObjectAnimator objectAnimator=ObjectAnimator.ofInt(btn,"background",R.mipmap.corectrect,R.mipmap.rectangle1);
        rightAnswerBtn.setBackgroundResource(R.mipmap.corectrect);
        rightAnswerBtn.setTextColor(Color.WHITE);
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rightAnswerBtn.setBackgroundResource(R.mipmap.rectangle1);
                rightAnswerBtn.setTextColor(Color.BLACK);
                // Time to load anim and shuffle
                loadAnim();
                if(quizCount==question.size()){
                    // Then the quiz has finished and then load to another screen for result

                }else{
                    quizCount++;
                    showNextQuiz();
                    //timerRunning=false;
                   // countDownTimer.start();
                    // I want to thank God for finding this solution although it took me like 5 days to discover not to use countDownTimer.start(). because other values won't be used again. Then timeleftinmills was initialized again
                    timeLeftInMills=11000;
                    countdown();
                }

            }
        },500);// half a seconds

    }
    public void scoreAnimation(int value){
        ValueAnimator animator=ValueAnimator.ofInt(Integer.parseInt(score.getText().toString()),Integer.parseInt(score.getText().toString())+value);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                score.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();

         animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                     public void onAnimationEnd(Animator animation) {
                        // Continue function call
                        rightAnswerCount=0;
                      }
                  });


    }

    public void bonusAnimation(){
        // This is to scale up and down of the bonus
        // make the bonus view to be visible
        // 1 display the bonus and update the bonus score
        // 2 scale down the bonus
        //   make the bonus view gone
        // 3 continue the score increase animation
        // the end.
        bonus.setVisibility(View.VISIBLE);
        // program here
        scaleUp=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scaleup);
        scaleDown=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scaledown);
        AnimationSet as=new AnimationSet(false);
        as.addAnimation(scaleUp);
        as.setDuration(1000);
        bonus.startAnimation(as);
       // bonus.setVisibility(View.GONE);

        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
           // Toast.makeText(getApplicationContext(),"scaleing has started",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            bonus.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        // An application can be paused and move the the destroy state
        // This in  turn doesnt clear the value from the onPause state as the onResume was called
        wasPaused(0);
    }


     @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

         // get 2d quiz array
         // and store 2d quiz array into a 1d arrayLIst
         ArrayList<String> oneDArray=new ArrayList<>();
         for(int a =0; a<quizArray.size();a++){
             //store value in a 1 d quiz array
             // loop the 1d quiz array and store in the final 1d quiz array
             // after the initial loop is complete then save in an arrayList.
             // for cols
             oneDArray=quizArray.get(a);
             for(String d: oneDArray){
                 System.out.println(d);
             }
             for(int c=0; c<oneDArray.size();c++){
                 savedInstance_quiz1d.add(oneDArray.get(c));
             }

         }


             outState.putInt(KEY_SCORE,Integer.parseInt(score.getText().toString()));
             outState.putInt(KEY_QUESTION_COUNT,quizCount);
             outState.putLong(KEY_MILLIS_LEFT,timeLeftInMills);
             outState.putString(KEY_QUESTION,questionLabel.getText().toString());
             outState.putString(Key_QUESTION1,qbtn1.getText().toString());
             outState.putString(Key_QUESTION2,qbtn2.getText().toString());
             outState.putString(Key_QUESTION3,qbtn3.getText().toString());
             outState.putString(Key_QUESTION4,qbtn4.getText().toString());
             outState.putString(KEY_RIGHTANSWER,rightAnswer);
             //outState.putBoolean(KEY_ANSWERED, );
             //outState.putParcelableArrayList(KEY_QUESTION_LIST,savedInstance_quiz1d);
             outState.putStringArrayList(KEY_QUESTION_LIST,savedInstance_quiz1d);
             savedInstance_quiz1d.size();
            //countDownTimer.cancel();
         // is timerRunning
        // outState.putBoolean("timerRunning",timerRunning);


     }


    @Override
    protected void onRestoreInstanceState(Bundle saved) {

    timeLeftInMills=saved.getLong(KEY_MILLIS_LEFT);


    }

    @Override
    protected void onPause(){
        super.onPause();
        mtimerModel.setTiemLeftInMilis(timeLeftInMills);
        timeLeftInMills=mtimerModel.getTiemLeftInMilis();
        Toast.makeText(getApplicationContext(),COUNTDOWN_IN_MILIS+"",Toast.LENGTH_SHORT).show();
        // to pause the background music when in onPuse mode
      backgroundMusic.pause();
        backgroundMusic.pauseDialogState=true;
    }
    @Override
    protected  void onResume(){
        super.onResume();
        if(backgroundMusic.state==true){
            backgroundMusic.resume();
        }
       // first stores a value in the sharedpreference when the application is puased
        // later checks it to either display the paused dialog when need be
        // SharedPreference didn't work so static class was better
        if(backgroundMusic.pauseDialogState){
            backgroundMusic.pauseDialogState=false;
            pauseDialog();

        }

    }
    // implement enable or disable sound
    // should add game music
    // This controlls the game sound

    public void playSound(int resource){
        final MediaPlayer mp=MediaPlayer.create(this,resource);
        if(sound_check==true){
            mp.start();
        }else if(sound_check==false){
            mp.stop();
        }

    }
         public void wasPaused(int n){
             SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("paused", MODE_PRIVATE);
             SharedPreferences.Editor editor=sharedPreferences.edit();
             editor.putInt("pd",n);
             editor.apply();
         }
         public boolean checkPaused(){
             SharedPreferences sp=getSharedPreferences("paused", MODE_PRIVATE);
             if(sp.getInt("pd",0)==1){
                 return true;
             }else{
                 return false;
             }
         }


}


package app.datableed.myapplication;

public class questionModel {
    // Question count should be gotten from ArrayList size
    private String questionLabel;
    private String rightAnswer;
    private String answer1;

    // used to initlaize the object
    public questionModel(String questionLabel, String rightAnswer, String answer1, String answer2, String answer3) {
        this.questionLabel = questionLabel;
        this.rightAnswer = rightAnswer;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
    }




    public String getQuestionLabel() {
        return questionLabel;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    private String answer2;
    private String answer3;


}

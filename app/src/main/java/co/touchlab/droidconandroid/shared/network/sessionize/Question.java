package co.touchlab.droidconandroid.shared.network.sessionize;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Question
{
    @NonNull
    private int    id;
    @Nullable
    private String question;
    @Nullable
    private int    questionType;
    @Nullable
    private String answer;
    @Nullable
    private int    sort;
    @Nullable
    private String answerExtra;

    @NonNull
    public int getId()
    {
        return id;
    }

    public void setId(@NonNull int id)
    {
        this.id = id;
    }

    @Nullable
    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(@Nullable String question)
    {
        this.question = question;
    }

    @Nullable
    public int getQuestionType()
    {
        return questionType;
    }

    public void setQuestionType(@Nullable int questionType)
    {
        this.questionType = questionType;
    }

    @Nullable
    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(@Nullable String answer)
    {
        this.answer = answer;
    }

    @Nullable
    public int getSort()
    {
        return sort;
    }

    public void setSort(@Nullable int sort)
    {
        this.sort = sort;
    }

    @Nullable
    public String getAnswerExtra()
    {
        return answerExtra;
    }

    public void setAnswerExtra(@Nullable String answerExtra)
    {
        this.answerExtra = answerExtra;
    }
}

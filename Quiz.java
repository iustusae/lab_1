import java.io.IOException;

public class Quiz {

    public static void init() throws IOException {
        Student student = new Student();
        Question question = new Question(student);

        question.quiz();
    }

    public static void init(int customNumberOfRetries) throws IOException {
        Student student = new Student();
        Question question = new Question(student, customNumberOfRetries);

        question.quiz();

    }
}

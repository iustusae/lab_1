import java.io.IOException;

/*
 * Wrapper class on the Question class to have one simple function in the Main method:
 *
 * Quiz.init();
 */
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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Represents the Question class
 *
 */
public class Question {
    /**
     * Non initialized holder for the operator that will be automatically generated
     */
    private char op = 0;

    /**
     * This variable holds the maximum cap for the Random numbers, for the sake of
     * removing redundancy and hardcoded values;
     */
    private final static int MAX_CAP = 100;

    /**
     * This variable holds the maximum allowed precision for the double values in ,
     * for the sake of
     * removing redundancy and hardcoded values;
     */
    private final static int MAX_DOUBLE_PRECISION = 2;

    /**
     * Character array that holds all accepted operators, used to pick a random
     * operator for the prompt
     */
    private char[] ops = { '+', '-', '*', '/' };

    private double right_operand = 0;
    private double left_operand = 0;

    /**
     * Holds the result of the generated operation. Used to compare it to the
     * student input later on
     */
    private double result = 0;

    /**
     * Injecting the `Student` class here for us to have access to it's credit and
     * modify the necessary properties of the Object
     */
    private final Student student;

    /**
     * Holds the student answer to the question, used to check it against
     * `this.result`
     */
    private double student_answer;

    /**
     * Since we aren't hardcoding the number of retries, this variable will holds
     * the number of retries allowed by the program. This variable is initialized in
     * the constructor
     */
    private int original_retries = 0;

    /**
     * This variable is the one being modified all over the program, being checked
     * against `original_retries`
     */
    private int retries = 0;

    /**
     * Since we aren't hardcoding the number of questions, the value of this
     * variable will be assigned from the user's input
     */
    private int number_of_questions = 0;

    /**
     * This is the Object used to read user inputted values from standard in.
     */
    private BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Constructor that takes in a Student, used to initialize the `student`,
     * `original_retries` and `retries` variables
     *
     * Gives the default amount of retries
     *
     * @param student The student who we wish to quiz
     */
    public Question(Student student) {
        this.student = student;
        this.original_retries = 3;

        /**
         * The reason for the double assignment here is that without this the value
         * stays at 0
         */
        this.retries = original_retries;
    }

    /**
     * Constructor for the Question class, takes in a student, and a
     * customNumberOfRetries
     *
     * @param student               The quizzed student
     * @param customNumberOfRetries The custom amount of retries
     */
    public Question(Student student, int customNumberOfRetries) {
        this.student = student;
        this.original_retries = customNumberOfRetries;
        this.retries = original_retries;
    }

    /**
     * This function picks the random operator for the later-on generated question.
     *
     * @return void
     */
    private void setOperator() {
        /** Use of the Java Random Object here */
        Random rd = new Random();
        int index = rd.nextInt(ops.length);
        this.op = ops[index];

        return;
    }

    /**
     * This function generates the Random left/right_operands for the question.
     * Since we are not hardcoding any values, the maximum cap of digits is
     * controlled by a separated static final int MAX_CAP
     */
    private void generateRandomOperands() {
        Random rd = new Random();
        this.right_operand = rd.nextInt(MAX_CAP);
        this.left_operand = rd.nextInt(MAX_CAP);
    }

    /**
     * This functions constructs the prompt for it to be printed to the student.
     * Note the use of the StringBuilder class.
     */
    private void printQuestionPrompt() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.right_operand).append(' ').append(this.op).append(' ').append(this.left_operand)
                .append(" = ?");
        System.out.println(builder.toString());
    }

    /**
     * This function pre-calculates the results to populate the `this.result`
     * variable, which will be used to check if the student's answer is true or aot
     */
    private void preCalculateResults() {
        switch (this.op) {
            case '+' -> this.result = (this.right_operand + this.left_operand);
            case '-' -> this.result = (this.right_operand - this.left_operand);
            case '*' -> this.result = (this.right_operand * this.left_operand);
            /**
             * The use of `BigDecimal` is only for it to restrain the values of the
             * divisions, in case of infinite number of digits
             * It also sets the precision to the variable `MAX_DOUBLE_PRECISION`
             */
            case '/' -> this.result = BigDecimal.valueOf((this.right_operand / this.left_operand))
                    .setScale(MAX_DOUBLE_PRECISION, RoundingMode.FLOOR).doubleValue();
            default -> System.err.println("Unknown operator: " + this.op);
        }
        System.out.println("Result is: " + result);
    }

    /**
     * This function gets and parses the user's input, it will give the value of
     * said input to `this.student_answer`
     */
    private void getResultFromStudent() {
        System.out.print("What's the answer: ");
        try {
            this.student_answer = Double.parseDouble(buff.readLine());
        } catch (NumberFormatException | IOException e) {
            System.out.println("The input you gave isn't a valid input, try again");
            getResultFromStudent();
        }
    }

    /**
     * This function simply checks that the correct result and the student's input
     * are equal
     *
     * @return boolean
     */
    private boolean checkIfAnswerIsCorrect() {
        return this.student_answer == this.result;
    }

    /**
     * This function abstracts the mechanism that updates the credit, in case the
     * student answers correctly the first time, he gets 1 added to the credit. Else
     * he gets only a fraction of it, depending on the number of retries wasted
     */
    private void updateStudentCredit() {
        if (this.retries == this.original_retries) {
            this.student.setCredit(this.student.getCredit() + 1);
        } else if (this.retries < this.original_retries) {
            this.student
                    .setCredit(this.student.getCredit() + Double.valueOf(1.0 / (this.original_retries - this.retries)));
        }
    }

    /**
     * This function abstracts over the mechanism of getting the student's input and
     * processing it.
     */
    private void getStudentAnswerAndCheckIfTrue() {
        getResultFromStudent();

        boolean is_student_correct = checkIfAnswerIsCorrect();

        if (is_student_correct) {
            updateStudentCredit();
            System.out.println("Congratulations, your answer is correct");
            ++this.student.good_answers;
            return;

        } else if (!is_student_correct && this.retries > 0) {
            System.out.println("Sadly, your answer is incorrect, you still have: " + this.retries + " retries!");
            --this.retries;
            getStudentAnswerAndCheckIfTrue();

        } else if (!is_student_correct && this.retries == 0) {
            System.out.println("\n\nIm sorry you have failed to answer the question: ");
            printQuestionPrompt();
            System.out.println("\t The answer was: " + String.valueOf(this.result));
            ++this.student.bad_answers;
        }

    }

    /**
     * This function is the one that sets the total number of questions.
     */
    private void setNumberOfQuestions() {
        System.out.print("How many questions do you want: ");
        try {
            this.number_of_questions = Integer.parseInt(buff.readLine());
            return;
        } catch (Exception e) {
            setNumberOfQuestions();
        }
    }

    /*
     * This function is an abstraction over all the random generation and everything
     * needed to ask a question.
     */
    private void askQuestion() {
        setOperator();
        generateRandomOperands();
        preCalculateResults();
        printQuestionPrompt();
        getStudentAnswerAndCheckIfTrue();

    }

    /*
     * This function is a clever usage of ANSI escape sequences to clear the console
     * to remove clutter
     */
    private final static void clearConsole() {
        System.out.print("\033\143");
    }

    /**
     * This function pretty prints the results with different prompts depending on
     * results etc...
     */
    private void displayResults() {
        clearConsole();

        System.out.println('\n' + this.student.name() + ", your results are: ");
        System.out.println("\t\tYou had " + student.good_answers + "/" + number_of_questions + " good answers!");
        if (student.bad_answers == 0 && student.getCredit() == number_of_questions) {
            System.out.println("\r\n\t\t\tPERFECT SCORE!!!");
        } else {
            System.out.println("\t\tYou had " + student.bad_answers + "/" + number_of_questions + " bad answers :(");
        }

        System.out.println("\n Overall, your credit is: "
                + BigDecimal.valueOf(student.getCredit()).setScale(MAX_DOUBLE_PRECISION, RoundingMode.FLOOR));
    }

    /*
     * This function is the only public function, abstracts over everything and asks
     * number_of_question questions, ends by displaying the results.
     */
    public void quiz() {
        setNumberOfQuestions();

        for (int i = 0; i < this.number_of_questions; ++i) {
            askQuestion();
            this.retries = original_retries;
            System.out.println("\n\n\n");
        }

        displayResults();
    }

}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Student {
    private double credit = 0;
    private String name = "";
    protected int good_answers = 0;
    protected int bad_answers = 0;

    private BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));

    public Student() throws IOException {
        System.out.print("What's your name: ");
        this.name = String.valueOf(buff.readLine()).strip();
    }

    /**
     * @return double The student's credit
     */
    public double getCredit() {
        return this.credit;
    }

    /**
     * Sets the new student's credit
     *
     * @param new_credit double
     */
    public void setCredit(double new_credit) {
        this.credit = new_credit;
        return;
    }

    /**
     *
     * @return String: this.name
     */
    public String name() {
        return this.name;
    }

}

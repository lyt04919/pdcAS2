package sims.model;

public class Grade {
    private String stuID;
    private String courseID;
    private double score;

    public Grade() {
    }

    public Grade(String stuID, String courseID, double score) {
        this.stuID = stuID;
        this.courseID = courseID;
        this.score = score;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString(){
        return stuID+","+courseID+","+score;
    }
}

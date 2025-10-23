package sims.model;

public class Student extends People{
    private String stuID;
    private String major;
    private String year;

    public Student() {
    }

    public Student(String stuID,String name,String gender, String major, String year) {
        super(name,gender);
        this.stuID = stuID;
        this.major = major;
        this.year = year;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString(){
        return stuID+","+getName()+","+getGender()+","+major+","+year;
    }
}

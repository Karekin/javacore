package io.github.dunwu.javacore.jvm.memory;

public class JVMCase {

    // 常量
    public final static String MAN_SEX_TYPE = "man";

    // 静态变量
    public static String WOMAN_SEX_TYPE = "woman";

    public static void main(String[] args) {

        StudentObject student = new StudentObject();
        student.setName("nick");
        student.setSexType(MAN_SEX_TYPE);
        student.setAge(20);

        JVMCase jvmPrinciples = new JVMCase();

        // 调用静态方法
        print(student);
        // 调用非静态方法
        jvmPrinciples.sayHello(student);
    }

    // 常规静态方法
    public static void print(StudentObject student) {
        System.out.println("name: " + student.getName() + "; sex:" + student.getSexType() + "; age:" + student.getAge());
    }

    // 非静态方法
    public void sayHello(StudentObject student) {
        System.out.println(student.getName() + " says: hello");
    }

    static class StudentObject {
        String name;
        String sexType;
        int age;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getSexType() {
            return sexType;
        }
        public void setSexType(String sexType) {
            this.sexType = sexType;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
    }
}




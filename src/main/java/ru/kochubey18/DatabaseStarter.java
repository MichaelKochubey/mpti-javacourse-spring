package ru.kochubey18;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

import javax.swing.plaf.nimbus.State;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ÄÇ 18
public class DatabaseStarter {
    @SneakyThrows
    public static void createDb(Connection connection) {
        Statement st = connection.createStatement();
        st.executeUpdate("DROP TABLE Student IF EXISTS");
        st.executeUpdate("DROP TABLE Vuz IF EXISTS");
        st.executeUpdate("CREATE TABLE Student(id INT PRIMARY KEY, name VARCHAR(20), vuzId INT)");
        st.executeUpdate("CREATE TABLE Vuz(id INT PRIMARY KEY, title VARCHAR(10), city VARCHAR(20))");
        st.executeUpdate("INSERT INTO Student VALUES(1, 'Mikhail', 1)");
        st.executeUpdate("INSERT INTO Student VALUES(2, 'Nikolay', 1)");
        st.executeUpdate("INSERT INTO Student VALUES(3, 'Alexey', 2)");
        st.executeUpdate("INSERT INTO Student VALUES(4, 'Vasilisa', 3)");
        st.executeUpdate("INSERT INTO Vuz VALUES(1, 'BMSTU', 'Moscow')");
        st.executeUpdate("INSERT INTO Vuz VALUES(2, 'ITMO', 'Piter')");
        st.executeUpdate("INSERT INTO Vuz VALUES(3, 'MPTI', 'Dolgoprudnyi')");
    }

    public static <T> List<T> findAll(Class<T> clz, Connection connection) {
        Field[] fields = clz.getDeclaredFields();
        String statementText="Select * from " + clz.getSimpleName();
        List<T> resList= new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet res= statement.executeQuery(statementText);
            while(res.next()) {
                T resObject = clz.newInstance();
                resList.add(resObject);
                for (Field f : fields) {
                    if(f.getType()==Integer.class){
                        f.set(resObject, res.getInt(f.getName()));
                    }
                    else f.set(resObject, res.getString(f.getName()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  resList;
    }

    public static <T> List<T> findAllCustom(Class<T> clz, Connection connection) {
        Field[] fields = clz.getDeclaredFields();
        String tableName = clz.getSimpleName();
        if (clz.isAnnotationPresent(TableName.class)) {
            TableName name = clz.getAnnotation(TableName.class);
            tableName = name.value();
        }
        String statementText="Select * from " + tableName;
        List<T> resList= new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet res= statement.executeQuery(statementText);
            while(res.next()) {
                T resObject = clz.newInstance();
                resList.add(resObject);
                for (Field f : fields) {
                    String fieldName = f.getName();
                    if (f.isAnnotationPresent(ColumnName.class)) {
                        ColumnName name = f.getAnnotation(ColumnName.class);
                        fieldName = name.value();
                    }

                    if (f.getType().equals(Integer.class)) {
                        f.set(resObject, res.getInt(fieldName));
                    } else if (f.getType().equals(String.class)) {
                        f.set(resObject, res.getString(fieldName));
                    } else if (f.getType().equals(Double.class)) {
                        f.set(resObject, res.getDouble(fieldName));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  resList;
    }

    @SneakyThrows
    public static void main(String[] args) {
        Class.forName("org.h2.Driver");
        Connection connection =
                DriverManager.getConnection("jdbc:h2:C:\\Users\\User\\IdeaProjects\\mpti-javacourse-spring\\DB");
        createDb(connection);

        List<Pupil> lst = findAllCustom(Pupil.class, connection);
        for (Pupil p : lst) {
            System.out.println(p);
        }
        System.out.println();
        List<Vuz> lst2 = findAllCustom(Vuz.class, connection);
        for (Vuz v : lst2) {
            System.out.println(v);
        }
    }
}

@ToString
@TableName("Student")
class Pupil {
    @Setter
    @Getter
    Integer id;

    @Setter
    @Getter
    String name;

    @Setter
    @Getter
    Integer vuzId;
}

@ToString
class Vuz {
    @Setter
    @Getter
    Integer id;

    @Setter
    @Getter
    @ColumnName("title")
    String name;

    @Setter
    @Getter
    String city;
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@interface TableName {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@interface ColumnName {
    String value();
}
package ru.ifmo.rain.kravchenko.student;

import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.join;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by Alexander on 04.03.2018.
 */

public class StudentDB implements StudentQuery {

    private List<String> foo(List<Student> students, Function<Student, String> f) {
        return students.stream().map(f).collect(toList());
    }

    private List<Student> bar(Collection<Student> students, Predicate<Student> f) {
        return sortStudentsByName(students.stream().filter(f).collect(toList()));
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return foo(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return foo(students, Student::getLastName);
    }

    @Override
    public List<String> getGroups(List<Student> students) {
        return foo(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return foo(students, e -> join(" ", e.getFirstName(), e.getLastName()));
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(toCollection(TreeSet::new));
    }

    @Override
    public String getMinStudentFirstName(List<Student> students) {
        return students.stream().min(Student::compareTo).map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return students.stream().sorted().collect(toList());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return students.stream().sorted(comparing(Student::getLastName).
                thenComparing(Student::getFirstName).thenComparingInt(Student::getId)).collect(toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return bar(students, e -> e.getFirstName().equals(name));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return bar(students, e -> e.getLastName().equals(name));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, String group) {
        return bar(students, e -> e.getGroup().equals(group));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, String group) {
        return students.stream().filter(e -> e.getGroup().equals(group)).
                collect(toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }

}

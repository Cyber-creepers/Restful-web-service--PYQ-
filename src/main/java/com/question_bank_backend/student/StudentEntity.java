package com.question_bank_backend.student;

import com.question_bank_backend.person.PersonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;


@Entity
@Table(name = "Student")
@PrimaryKeyJoinColumn(name = "ID")
public class StudentEntity extends PersonEntity
{

}

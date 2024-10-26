package com.question_bank_backend.superadmin;

import com.question_bank_backend.person.PersonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;


@Entity
@Table(name = "SuperAdmin")
@PrimaryKeyJoinColumn(name = "ID")
public class SuperAdminEntity extends PersonEntity
{


}

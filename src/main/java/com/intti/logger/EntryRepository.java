/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.intti.logger;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Lenovo
 */
public interface EntryRepository extends JpaRepository<Entry, Long> {    
}

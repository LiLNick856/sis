
package com.mgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import com.mgs.model.Employee;
import com.mgs.repository.EmployeeRepository;

@SpringBootApplication
public class EmployeeManagementApplication implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Populando o banco de dados com alguns funcionários de exemplo
        employeeRepository.save(new Employee("John Doe", "johndoe@example.com", "123456789", "None", "None"));
        employeeRepository.save(new Employee("Jane Smith", "janesmith@example.com", "987654321", "Peanuts", "Asthma"));
        employeeRepository.save(new Employee("Peter Jones", "peterjones@example.com", "1122334455", "None", "Diabetes"));
    }
}

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String allergies;
    private String medicalConditions;

    // Getters and Setters
    // ...
}

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Employee employee = employeeService.getEmployeeById(id);
        employee.setName(employeeDetails.getName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhone(employeeDetails.getPhone());
        employee.setAllergies(employeeDetails.getAllergies());
        employee.setMedicalConditions(employeeDetails.getMedicalConditions());
        return employeeService.saveEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
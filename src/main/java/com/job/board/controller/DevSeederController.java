package com.job.board.controller;

import com.job.board.entity.JobCategory;
import com.job.board.entity.JobTag;
import com.job.board.entity.User;
import com.job.board.enums.Role;
import com.job.board.repository.JobCategoryRepository;
import com.job.board.repository.JobTagRepository;
import com.job.board.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/dev-seeder")
public class DevSeederController {

    private final UserRepository userRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JobTagRepository jobTagRepository;

    public DevSeederController(UserRepository userRepository, JobCategoryRepository jobCategoryRepository, PasswordEncoder passwordEncoder, JobTagRepository jobTagRepository) {
        this.userRepository = userRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.jobTagRepository = jobTagRepository;
    }

    @GetMapping("/seed-admin")
    @ResponseBody
    public String seedAdmin() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Umar");
            admin.setEmail("umarsabirin369@gmail.com");
            admin.setLastName("Sabirin");
            admin.setUsername("admin");
            admin.setRole(Role.ADMIN);
            admin.setPassword(passwordEncoder.encode("passwordsecret"));

            userRepository.save(admin);
            return "✅ Admin user seeded.";
        }

        return "ℹ️ Admin user already exists.";
    }

    @GetMapping("/seed-job-category")
    @ResponseBody
    public String seedJobCategory() {
        if (jobCategoryRepository.count() == 0) {
            jobCategoryRepository.save(new JobCategory("IT"));
            jobCategoryRepository.save(new JobCategory("Marketing"));
            jobCategoryRepository.save(new JobCategory("Finance"));
            jobCategoryRepository.save(new JobCategory("HR"));
            jobCategoryRepository.save(new JobCategory("Design"));

            jobCategoryRepository.save(new JobCategory("Data Science"));
            jobCategoryRepository.save(new JobCategory("Cybersecurity"));
            jobCategoryRepository.save(new JobCategory("Frontend Developer"));
            jobCategoryRepository.save(new JobCategory("Backend Developer"));
            jobCategoryRepository.save(new JobCategory("Fullstack Developer"));

            jobCategoryRepository.save(new JobCategory("Mobile Developer"));
            jobCategoryRepository.save(new JobCategory("DevOps Engineer"));
            jobCategoryRepository.save(new JobCategory("QA Engineer"));
            jobCategoryRepository.save(new JobCategory("UI/UX Designer"));
            jobCategoryRepository.save(new JobCategory("Product Manager"));

            jobCategoryRepository.save(new JobCategory("Project Manager"));
            jobCategoryRepository.save(new JobCategory("Business Analyst"));
            jobCategoryRepository.save(new JobCategory("SEO Specialist"));
            jobCategoryRepository.save(new JobCategory("Content Writer"));
            jobCategoryRepository.save(new JobCategory("Social Media Manager"));

            jobCategoryRepository.save(new JobCategory("Digital Marketing"));
            jobCategoryRepository.save(new JobCategory("Copywriter"));
            jobCategoryRepository.save(new JobCategory("Sales Executive"));
            jobCategoryRepository.save(new JobCategory("Customer Support"));
            jobCategoryRepository.save(new JobCategory("Technical Support"));

            jobCategoryRepository.save(new JobCategory("Accounting"));
            jobCategoryRepository.save(new JobCategory("Auditor"));
            jobCategoryRepository.save(new JobCategory("Banking"));
            jobCategoryRepository.save(new JobCategory("Investment Analyst"));
            jobCategoryRepository.save(new JobCategory("Tax Consultant"));

            jobCategoryRepository.save(new JobCategory("Legal Advisor"));
            jobCategoryRepository.save(new JobCategory("Paralegal"));
            jobCategoryRepository.save(new JobCategory("Logistics"));
            jobCategoryRepository.save(new JobCategory("Supply Chain Manager"));
            jobCategoryRepository.save(new JobCategory("Procurement"));

            jobCategoryRepository.save(new JobCategory("Manufacturing"));
            jobCategoryRepository.save(new JobCategory("Mechanical Engineer"));
            jobCategoryRepository.save(new JobCategory("Electrical Engineer"));
            jobCategoryRepository.save(new JobCategory("Civil Engineer"));
            jobCategoryRepository.save(new JobCategory("Chemical Engineer"));

            jobCategoryRepository.save(new JobCategory("Architecture"));
            jobCategoryRepository.save(new JobCategory("Construction"));
            jobCategoryRepository.save(new JobCategory("Real Estate Agent"));
            jobCategoryRepository.save(new JobCategory("Healthcare"));
            jobCategoryRepository.save(new JobCategory("Nurse"));

            jobCategoryRepository.save(new JobCategory("Doctor"));
            jobCategoryRepository.save(new JobCategory("Pharmacist"));
            jobCategoryRepository.save(new JobCategory("Lab Technician"));
            jobCategoryRepository.save(new JobCategory("Veterinarian"));
            jobCategoryRepository.save(new JobCategory("Psychologist"));

            jobCategoryRepository.save(new JobCategory("Teacher"));
            jobCategoryRepository.save(new JobCategory("Lecturer"));
            jobCategoryRepository.save(new JobCategory("Researcher"));
            jobCategoryRepository.save(new JobCategory("Translator"));
            jobCategoryRepository.save(new JobCategory("Librarian"));

            jobCategoryRepository.save(new JobCategory("Event Planner"));
            jobCategoryRepository.save(new JobCategory("Hospitality"));
            jobCategoryRepository.save(new JobCategory("Travel Consultant"));
            jobCategoryRepository.save(new JobCategory("Chef"));
            jobCategoryRepository.save(new JobCategory("Bartender"));

            jobCategoryRepository.save(new JobCategory("Fitness Trainer"));
            jobCategoryRepository.save(new JobCategory("Photographer"));
            jobCategoryRepository.save(new JobCategory("Video Editor"));
            jobCategoryRepository.save(new JobCategory("Animator"));
            jobCategoryRepository.save(new JobCategory("Game Developer"));
            return "✅ Job Category Seeded.";
        }
        return "ℹ️ Job Category Already Exists In The DB.";

    }

    @GetMapping("/seed-job-tag")
    @ResponseBody
    public String seedJobTag() {
        if (jobTagRepository.count() == 0) {
            List<String> baseTags = Arrays.asList(
                    "Java", "Spring Boot", "Java SE", "Java EE", "JPA", "Hibernate", "REST API", "SOAP Web Services",
                    "Maven", "Gradle", "JUnit", "Mockito", "Python", "Django", "Flask", "FastAPI", "NumPy", "Pandas",
                    "Laravel", "PHP", "Composer", "Blade Template", "React", "React Native", "Redux", "Vue.js",
                    "Vuex", "Nuxt.js", "Angular", "RxJS", "TypeScript", "Next.js", "HTML", "CSS", "SASS", "LESS",
                    "JavaScript", "ES6", "Node.js", "Express.js", "NestJS", "MongoDB", "MySQL", "PostgreSQL", "SQLite",
                    "Redis", "Elasticsearch", "Firebase", "Firestore", "AWS EC2", "AWS S3", "AWS Lambda", "Azure DevOps",
                    "Azure Blob Storage", "GCP", "Heroku", "DigitalOcean", "Docker", "Docker Compose", "Kubernetes",
                    "Helm", "Terraform", "Ansible", "Linux", "Nginx", "Apache", "Jenkins", "Git", "GitHub", "GitLab",
                    "Bitbucket", "CI/CD", "Agile", "Scrum", "Kanban", "JIRA", "Trello", "Notion", "UI/UX", "Figma",
                    "Sketch", "Adobe XD", "Zeplin", "InVision", "Bootstrap", "Tailwind CSS", "Material UI", "Ant Design",
                    "Chakra UI", "Chart.js", "D3.js", "Three.js", "WebSockets", "WebRTC", "OAuth2", "JWT", "SAML",
                    "Selenium", "Cypress", "Playwright", "Postman", "Insomnia", "Swagger", "OpenAPI", "Kafka",
                    "RabbitMQ", "gRPC", "GraphQL", "Apollo Client", "Apollo Server", "Prisma", "Sequelize", "TypeORM",
                    "Data Analysis", "Data Science", "Machine Learning", "Deep Learning", "Scikit-learn", "TensorFlow",
                    "Keras", "PyTorch", "OpenCV", "NLTK", "Spacy", "Power BI", "Tableau", "Excel", "ETL", "Airflow",
                    "BigQuery", "Snowflake", "Hadoop", "Spark", "Blockchain", "Solidity", "Web3.js", "Hardhat",
                    "Cybersecurity", "Penetration Testing", "Nmap", "Wireshark", "Burp Suite", "OWASP", "ISO 27001",
                    "Network Security", "DevSecOps", "QA Testing", "Manual Testing", "Automation Testing",
                    "Behavior-Driven Development", "Test-Driven Development", "Mobile Development", "Android",
                    "iOS", "Flutter", "Swift", "Kotlin", "Xamarin", "Unity", "Unreal Engine", "Game Development",
                    "E-commerce", "Shopify", "WooCommerce", "Magento", "Salesforce", "ERP", "CRM", "HRIS", "Zoho",
                    "Digital Marketing", "SEO", "SEM", "Google Ads", "Facebook Ads", "Social Media", "Content Marketing",
                    "Email Marketing", "Google Analytics", "Hotjar", "Content Writing", "Copywriting", "Technical Writing",
                    "Product Management", "Business Analysis", "Stakeholder Management", "Wireframing", "Prototyping",
                    "System Design", "Software Architecture", "Microservices", "Monolith", "Event-Driven Architecture",
                    "Clean Architecture", "Domain-Driven Design", "CQRS", "MVC", "MVVM", "Version Control", "Open Source"
            );

            int counter = 1;
            while (counter <= 200) {
                for (String tag : baseTags) {
                    if (counter > 200) break;
                    jobTagRepository.save(new JobTag(tag + " " + counter));
                    counter++;
                }
            }
            return "✅ Job Tag Seeded.";
        }
        return "ℹ️ Job Tags Already Exists In The DB.";
    }
}

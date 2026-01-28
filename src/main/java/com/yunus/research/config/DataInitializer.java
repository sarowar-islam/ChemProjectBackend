package com.yunus.research.config;

import com.yunus.research.entity.*;
import com.yunus.research.repository.*;
import com.yunus.research.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

        private final AdminRepository adminRepository;
        private final MemberRepository memberRepository;
        private final ProjectRepository projectRepository;
        private final PublicationRepository publicationRepository;
        private final NoticeRepository noticeRepository;
        private final NewsRepository newsRepository;
        private final SiteSettingsRepository siteSettingsRepository;
        private final PasswordService passwordService;

        @Override
        public void run(String... args) {
                // Only seed admin if no admin exists
                if (adminRepository.count() == 0) {
                        log.info("Seeding admin user...");
                        seedAdmin();
                        log.info("Admin user seeding completed!");
                } else {
                        log.info("Admin already exists, skipping admin seed.");
                }

                // Seed other data only if they don't exist
                if (projectRepository.count() == 0) {
                        seedProjects();
                }
                if (noticeRepository.count() == 0) {
                        seedNotices();
                }
                if (newsRepository.count() == 0) {
                        seedNews();
                }
                if (!siteSettingsRepository.existsById("default")) {
                        seedSiteSettings();
                }
        }

        private void seedAdmin() {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setEmail("yunus@cuet.ac.bd");
                admin.setPassword(passwordService.hashPassword("admin123"));
                adminRepository.save(admin);
                log.info("Admin user created: yunus@cuet.ac.bd");
        }

        private void seedMembers() {
                // Dr. Yunus Ahmed - Principal Investigator
                Member yunus = new Member();
                yunus.setUsername("dr.yunus");
                yunus.setName("Prof. Dr. Yunus Ahmed");
                yunus.setEmail("yunus@cuet.ac.bd");
                yunus.setPassword(passwordService.hashPassword("member123"));
                yunus.setPhone("+880-31-714946");
                yunus.setDesignation("Professor & Principal Investigator");
                yunus.setResearchArea(
                                "Environmental Remediation, Resource Recovery, Nanomaterials, Wastewater Treatment");
                yunus.setBio(
                                "Prof. Dr. Yunus Ahmed is a distinguished Professor at the Department of Chemistry, Chittagong University of Engineering & Technology (CUET). He specializes in environmental remediation, resource recovery, nanomaterials, and cutting-edge wastewater treatment technologies. With extensive research experience and numerous publications in high-impact journals, he leads the Environmental Chemistry Research Group at CUET.");
                yunus.setPhotoUrl(
                                "https://scholar.googleusercontent.com/citations?view_op=view_photo&user=5oILmB0AAAAJ&citpid=3");
                yunus.setGoogleScholarLink("https://scholar.google.com/citations?user=5oILmB0AAAAJ&hl=en");
                yunus.setExpertise(Arrays.asList("Environmental Remediation", "Resource Recovery", "Nanomaterials",
                                "Wastewater Treatment", "Green Chemistry", "Analytical Chemistry"));
                yunus.setJoinedDate(LocalDate.of(2005, 1, 15));
                memberRepository.save(yunus);

                // Research Team Members
                List<Member> members = Arrays.asList(
                                createMember("dr.rahman", "Dr. Abdul Rahman", "rahman@cuet.ac.bd",
                                                "Associate Professor",
                                                "Organic Synthesis & Green Chemistry",
                                                "Dr. Rahman specializes in organic synthesis with focus on green chemistry approaches for sustainable chemical processes.",
                                                "https://scholar.google.com/citations?user=dkC9aFEAAAAJ",
                                                Arrays.asList("Organic Synthesis", "Green Chemistry", "Catalysis"),
                                                LocalDate.of(2010, 6, 1)),

                                createMember("dr.fatima", "Dr. Fatima Sultana", "fatima@cuet.ac.bd",
                                                "Associate Professor",
                                                "Computational Chemistry & Drug Design",
                                                "Dr. Fatima works on computational approaches to drug discovery and molecular modeling.",
                                                "https://scholar.google.com/citations?user=B7vSqZsAAAAJ",
                                                Arrays.asList("Computational Chemistry", "Drug Design",
                                                                "Molecular Modeling"),
                                                LocalDate.of(2012, 3, 15)),

                                createMember("dr.kamal", "Dr. Md. Kamal Hossain", "kamal@cuet.ac.bd",
                                                "Assistant Professor",
                                                "Electrochemistry & Energy Storage",
                                                "Dr. Kamal focuses on electrochemical energy storage systems and battery technologies.",
                                                "https://scholar.google.com/citations?user=kT8DgFAAAAAJ",
                                                Arrays.asList("Electrochemistry", "Energy Storage", "Batteries"),
                                                LocalDate.of(2015, 8, 20)),

                                createMember("dr.nasrin", "Dr. Nasrin Akter", "nasrin@cuet.ac.bd",
                                                "Assistant Professor",
                                                "Polymer Chemistry & Biomaterials",
                                                "Dr. Nasrin researches polymer-based biomaterials for biomedical applications.",
                                                "https://scholar.google.com/citations?user=RfVlf5gAAAAJ",
                                                Arrays.asList("Polymer Chemistry", "Biomaterials", "Drug Delivery"),
                                                LocalDate.of(2016, 1, 10)),

                                createMember("rafiq.ahmed", "Rafiq Ahmed", "rafiq@cuet.ac.bd", "PhD Researcher",
                                                "Nanomaterial Synthesis for Water Treatment",
                                                "Rafiq is working on his PhD focusing on novel nanomaterials for heavy metal removal from wastewater.",
                                                "https://scholar.google.com/citations?user=QfVKrb8AAAAJ",
                                                Arrays.asList("Nanomaterials", "Water Treatment",
                                                                "Heavy Metal Removal"),
                                                LocalDate.of(2020, 9, 1)),

                                createMember("salma.begum", "Salma Begum", "salma@cuet.ac.bd", "PhD Researcher",
                                                "Photocatalysis & Environmental Remediation",
                                                "Salma researches photocatalytic degradation of organic pollutants in water systems.",
                                                "https://scholar.google.com/citations?user=5TcdEOsAAAAJ",
                                                Arrays.asList("Photocatalysis", "Environmental Chemistry",
                                                                "Organic Pollutants"),
                                                LocalDate.of(2021, 1, 15)),

                                createMember("tanvir.islam", "Tanvir Islam", "tanvir@cuet.ac.bd", "PhD Researcher",
                                                "Biosensors & Analytical Chemistry",
                                                "Tanvir develops biosensors for environmental and biomedical monitoring applications.",
                                                "https://scholar.google.com/citations?user=JvBz_Q4AAAAJ",
                                                Arrays.asList("Biosensors", "Analytical Chemistry",
                                                                "Environmental Monitoring"),
                                                LocalDate.of(2021, 6, 1)),

                                createMember("ayesha.khatun", "Ayesha Khatun", "ayesha@cuet.ac.bd", "MSc Researcher",
                                                "Adsorption Studies & Water Purification",
                                                "Ayesha studies adsorption mechanisms for water purification using low-cost materials.",
                                                "https://scholar.google.com/citations?user=GxMUTBwAAAAJ",
                                                Arrays.asList("Adsorption", "Water Purification", "Low-cost Materials"),
                                                LocalDate.of(2022, 1, 10)),

                                createMember("imran.hossain", "Imran Hossain", "imran@cuet.ac.bd", "MSc Researcher",
                                                "Green Synthesis of Nanoparticles",
                                                "Imran focuses on eco-friendly synthesis routes for metal oxide nanoparticles.",
                                                "https://scholar.google.com/citations?user=8pXhbC0AAAAJ",
                                                Arrays.asList("Green Synthesis", "Nanoparticles", "Metal Oxides"),
                                                LocalDate.of(2022, 6, 15)),

                                createMember("nusrat.jahan", "Nusrat Jahan", "nusrat@cuet.ac.bd", "MSc Researcher",
                                                "Membrane Technology for Water Treatment",
                                                "Nusrat researches membrane-based separation technologies for water treatment.",
                                                "https://scholar.google.com/citations?user=Xm3bP48AAAAJ",
                                                Arrays.asList("Membrane Technology", "Separation Processes",
                                                                "Water Treatment"),
                                                LocalDate.of(2023, 1, 5)),

                                createMember("fahim.shahriar", "Fahim Shahriar", "fahim@cuet.ac.bd",
                                                "Research Assistant",
                                                "Spectroscopic Analysis",
                                                "Fahim assists in spectroscopic characterization of synthesized materials.",
                                                "https://scholar.google.com/citations?user=P5KlJXEAAAAJ",
                                                Arrays.asList("Spectroscopy", "Material Characterization",
                                                                "UV-Vis Analysis"),
                                                LocalDate.of(2023, 6, 1)),

                                createMember("sadia.afrin", "Sadia Afrin", "sadia@cuet.ac.bd", "Research Assistant",
                                                "Environmental Sample Analysis",
                                                "Sadia conducts environmental sample collection and analysis for various research projects.",
                                                "https://scholar.google.com/citations?user=K7mNxb4AAAAJ",
                                                Arrays.asList("Environmental Analysis", "Sample Preparation",
                                                                "Quality Control"),
                                                LocalDate.of(2023, 9, 1)),

                                createMember("mehedi.hasan", "Mehedi Hasan", "mehedi@cuet.ac.bd", "BSc Student",
                                                "Undergraduate Research",
                                                "Mehedi is an undergraduate student contributing to nanomaterial synthesis projects.",
                                                "https://scholar.google.com/citations?user=L9nKpQEAAAAJ",
                                                Arrays.asList("Undergraduate Research", "Nanomaterials"),
                                                LocalDate.of(2024, 1, 15)),

                                createMember("tasnim.rahman", "Tasnim Rahman", "tasnim@cuet.ac.bd", "BSc Student",
                                                "Undergraduate Research",
                                                "Tasnim assists in water quality analysis and environmental monitoring projects.",
                                                "https://scholar.google.com/citations?user=M2oLqRIAAAAJ",
                                                Arrays.asList("Water Quality", "Environmental Monitoring"),
                                                LocalDate.of(2024, 6, 1)));

                memberRepository.saveAll(members);
                log.info("Created {} team members", members.size() + 1);
        }

        private Member createMember(String username, String name, String email, String designation,
                        String researchArea, String bio, String scholarLink,
                        List<String> expertise, LocalDate joinedDate) {
                Member member = new Member();
                member.setUsername(username);
                member.setName(name);
                member.setEmail(email);
                member.setPassword(passwordService.hashPassword("member123"));
                member.setDesignation(designation);
                member.setResearchArea(researchArea);
                member.setBio(bio);
                member.setPhotoUrl("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400");
                member.setGoogleScholarLink(scholarLink);
                member.setExpertise(expertise);
                member.setJoinedDate(joinedDate);
                return member;
        }

        private void seedProjects() {
                List<Project> projects = Arrays.asList(
                                createProject("Development of Novel Adsorbents for Heavy Metal Removal from Industrial Wastewater",
                                                "This project focuses on synthesizing and characterizing novel nano-adsorbents for efficient removal of heavy metals (Pb, Cd, Cr, As) from industrial wastewater. The research aims to develop cost-effective and environmentally friendly solutions for water treatment in Bangladesh's industrial zones.",
                                                "https://www.researchgate.net/project/heavy-metal-removal",
                                                Project.ProjectStatus.ONGOING, LocalDate.of(2023, 1, 1), null),

                                createProject("Photocatalytic Degradation of Textile Dyes Using TiO2-based Nanocomposites",
                                                "Investigation of titanium dioxide-based nanocomposites for photocatalytic degradation of organic dyes from textile industry effluents. The project explores visible-light-driven photocatalysis for sustainable wastewater treatment.",
                                                "https://www.researchgate.net/project/photocatalytic-dyes",
                                                Project.ProjectStatus.ONGOING, LocalDate.of(2022, 6, 15), null),

                                createProject("Resource Recovery from Electronic Waste: A Green Chemistry Approach",
                                                "Development of hydrometallurgical processes for recovering valuable metals from electronic waste using environmentally benign leaching agents. Focus on gold, silver, copper, and rare earth elements recovery.",
                                                "https://www.researchgate.net/project/ewaste-recovery",
                                                Project.ProjectStatus.ONGOING, LocalDate.of(2024, 1, 1), null),

                                createProject("Biosynthesis of Silver Nanoparticles for Antimicrobial Applications",
                                                "Green synthesis of silver nanoparticles using plant extracts and their application in antimicrobial coatings and water disinfection systems.",
                                                "https://www.researchgate.net/project/agnp-biosynthesis",
                                                Project.ProjectStatus.COMPLETED, LocalDate.of(2021, 3, 1),
                                                LocalDate.of(2023, 12, 31)),

                                createProject("Development of Low-cost Arsenic Removal Filters for Rural Bangladesh",
                                                "Design and field testing of affordable arsenic removal filters using locally available materials for providing safe drinking water to arsenic-affected rural communities.",
                                                "https://www.researchgate.net/project/arsenic-filter",
                                                Project.ProjectStatus.COMPLETED, LocalDate.of(2019, 6, 1),
                                                LocalDate.of(2022, 5, 31)),

                                createProject("Electrochemical Sensors for Real-time Water Quality Monitoring",
                                                "Development of electrochemical sensor arrays for continuous monitoring of water quality parameters including pH, dissolved oxygen, heavy metals, and organic pollutants.",
                                                "https://www.researchgate.net/project/water-sensors",
                                                Project.ProjectStatus.ONGOING, LocalDate.of(2023, 9, 1), null),

                                createProject("Microplastics Detection and Removal from Aquatic Systems",
                                                "Research on detection methods and removal technologies for microplastics contamination in freshwater and marine environments of Bangladesh.",
                                                "https://www.researchgate.net/project/microplastics",
                                                Project.ProjectStatus.ONGOING, LocalDate.of(2024, 3, 1), null),

                                createProject("Sustainable Synthesis of Carbon Quantum Dots for Sensing Applications",
                                                "Green synthesis of carbon quantum dots from agricultural waste and their application in fluorescence-based sensing of environmental pollutants.",
                                                "https://www.researchgate.net/project/carbon-dots",
                                                Project.ProjectStatus.COMPLETED, LocalDate.of(2020, 1, 15),
                                                LocalDate.of(2022, 12, 31)));

                projectRepository.saveAll(projects);
                log.info("Created {} projects", projects.size());
        }

        private Project createProject(String title, String description, String link,
                        Project.ProjectStatus status, LocalDate startDate, LocalDate endDate) {
                Project project = new Project();
                project.setTitle(title);
                project.setDescription(description);
                project.setResearchLink(link);
                project.setStatus(status);
                project.setStartDate(startDate);
                project.setEndDate(endDate);
                return project;
        }

        private void seedPublications() {
                List<Publication> publications = Arrays.asList(
                                createPublication(
                                                "Efficient removal of lead(II) from aqueous solution using novel graphene oxide-magnetite nanocomposite",
                                                Arrays.asList("Y. Ahmed", "M. Rahman", "F. Sultana"), 2024,
                                                "Journal of Environmental Chemical Engineering", 156),

                                createPublication(
                                                "Photocatalytic degradation of methylene blue using TiO2/ZnO nanocomposites under visible light irradiation",
                                                Arrays.asList("Y. Ahmed", "S. Begum", "K. Hossain"), 2024,
                                                "Applied Surface Science", 89),

                                createPublication(
                                                "Green synthesis of silver nanoparticles using Azadirachta indica leaf extract and their antimicrobial activity",
                                                Arrays.asList("Y. Ahmed", "N. Akter", "R. Ahmed"), 2023,
                                                "Journal of Nanomaterials", 234),

                                createPublication(
                                                "Recovery of copper and gold from electronic waste using environmentally friendly leaching agents",
                                                Arrays.asList("Y. Ahmed", "T. Islam", "A. Rahman"), 2023,
                                                "Waste Management", 178),

                                createPublication(
                                                "Development of low-cost arsenic removal filter using iron-coated sand: Field performance evaluation",
                                                Arrays.asList("Y. Ahmed", "A. Khatun", "I. Hossain"), 2023,
                                                "Journal of Water Process Engineering", 145),

                                createPublication(
                                                "Electrochemical detection of heavy metals in water using modified carbon paste electrode",
                                                Arrays.asList("Y. Ahmed", "T. Islam", "F. Sultana"), 2022,
                                                "Sensors and Actuators B: Chemical",
                                                201),

                                createPublication(
                                                "Adsorption of chromium(VI) onto activated carbon derived from agricultural waste: Kinetics and isotherm studies",
                                                Arrays.asList("Y. Ahmed", "N. Jahan", "M. Hasan"), 2022,
                                                "Environmental Technology & Innovation", 167),

                                createPublication(
                                                "Microplastics in freshwater ecosystems of Bangladesh: Occurrence, distribution and ecological risks",
                                                Arrays.asList("Y. Ahmed", "S. Afrin", "R. Ahmed"), 2024,
                                                "Science of The Total Environment",
                                                78),

                                createPublication(
                                                "Synthesis and characterization of carbon quantum dots from banana peel waste for fluorescent sensing",
                                                Arrays.asList("Y. Ahmed", "I. Hossain", "K. Hossain"), 2022,
                                                "Materials Today Communications",
                                                134),

                                createPublication(
                                                "A review on nanomaterial-based adsorbents for the removal of emerging contaminants from water",
                                                Arrays.asList("Y. Ahmed", "F. Sultana", "A. Rahman"), 2023,
                                                "Chemical Engineering Journal",
                                                412),

                                createPublication(
                                                "Biosorption of textile dyes using dead biomass of Aspergillus niger: Optimization and mechanism",
                                                Arrays.asList("Y. Ahmed", "S. Begum", "N. Akter"), 2021,
                                                "Journal of Environmental Management",
                                                189),

                                createPublication(
                                                "Membrane distillation for treatment of textile wastewater: Performance and fouling analysis",
                                                Arrays.asList("Y. Ahmed", "N. Jahan", "T. Rahman"), 2021,
                                                "Separation and Purification Technology", 156));

                publicationRepository.saveAll(publications);
                log.info("Created {} publications", publications.size());
        }

        private Publication createPublication(String title, List<String> authors, int year,
                        String journal, int citedBy) {
                Publication pub = new Publication();
                pub.setTitle(title);
                pub.setAuthors(authors);
                pub.setYear(year);
                pub.setJournal(journal);
                pub.setCitedBy(citedBy);
                return pub;
        }

        private void seedNotices() {
                List<Notice> notices = Arrays.asList(
                                createNotice("PhD Position Available - Environmental Chemistry",
                                                "We are looking for motivated PhD candidates to join our research group. The position focuses on nanomaterial synthesis for water treatment applications. Candidates should have MSc in Chemistry/Chemical Engineering with good academic record. Funded positions available for Bangladeshi students. Application deadline: March 31, 2026.",
                                                Notice.NoticePriority.IMPORTANT),

                                createNotice("Weekly Research Seminar Schedule",
                                                "Our weekly research seminar will be held every Thursday at 3:00 PM in the Chemistry Conference Room (Room 301). All group members are required to attend. External participants are welcome. Contact: yunus@cuet.ac.bd",
                                                Notice.NoticePriority.NORMAL),

                                createNotice("Laboratory Safety Training - Mandatory",
                                                "All new research group members must complete the laboratory safety training before starting experimental work. Training sessions are held on the first Monday of each month at 10:00 AM. Registration required.",
                                                Notice.NoticePriority.IMPORTANT),

                                createNotice("Research Grant Application Deadline",
                                                "Reminder: The deadline for HEQEP research grant application is February 28, 2026. All faculty members interested in applying should submit their proposals to the group coordinator by February 15.",
                                                Notice.NoticePriority.NORMAL),

                                createNotice("Instrument Booking System Update",
                                                "The online booking system for shared instruments (UV-Vis, FTIR, XRD) has been updated. Please use the new portal at instruments.cuet.ac.bd. Old bookings have been migrated automatically.",
                                                Notice.NoticePriority.NORMAL));

                noticeRepository.saveAll(notices);
                log.info("Created {} notices", notices.size());
        }

        private Notice createNotice(String title, String content, Notice.NoticePriority priority) {
                Notice notice = new Notice();
                notice.setTitle(title);
                notice.setContent(content);
                notice.setPriority(priority);
                return notice;
        }

        private void seedNews() {
                List<News> newsItems = Arrays.asList(
                                createNews("Research Paper Published in Journal of Environmental Chemical Engineering",
                                                "Our latest research on graphene oxide nanocomposites for heavy metal removal has been published.",
                                                "We are pleased to announce that our research paper titled 'Efficient removal of lead(II) from aqueous solution using novel graphene oxide-magnetite nanocomposite' has been published in the Journal of Environmental Chemical Engineering (Impact Factor: 7.4). This work represents a significant advancement in water treatment technology and demonstrates the potential of nanomaterials for environmental remediation. Congratulations to all contributing researchers!",
                                                "https://images.unsplash.com/photo-1532094349884-543bc11b234d?w=800",
                                                "Prof. Dr. Yunus Ahmed"),

                                createNews("Dr. Yunus Ahmed Receives Best Researcher Award",
                                                "Prof. Dr. Yunus Ahmed has been honored with the Best Researcher Award 2025 by CUET.",
                                                "Chittagong University of Engineering & Technology has recognized Prof. Dr. Yunus Ahmed with the prestigious Best Researcher Award 2025 for his outstanding contributions to environmental chemistry research. The award ceremony was held during the annual university convocation. This recognition highlights over two decades of dedicated research in water treatment and environmental remediation technologies.",
                                                "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=800",
                                                "CUET News"),

                                createNews("International Collaboration with University of Tokyo",
                                                "New research collaboration established with the University of Tokyo on nanomaterial research.",
                                                "Our research group has established a formal collaboration agreement with the Department of Chemical Engineering at the University of Tokyo. This partnership will focus on developing advanced nanomaterials for environmental applications. The collaboration includes student exchange programs, joint research projects, and co-authored publications. Japanese researchers will visit CUET in March 2026.",
                                                "https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=800",
                                                "Prof. Dr. Yunus Ahmed"),

                                createNews("Successful Completion of Arsenic Filter Project",
                                                "Our low-cost arsenic filter project has successfully provided safe water to 500 rural households.",
                                                "We are proud to announce the successful completion of our three-year project on developing low-cost arsenic removal filters for rural Bangladesh. The project has installed filters in 500 households across arsenic-affected areas in Comilla and Chandpur districts. Follow-up studies show significant improvement in water quality and reduction in arsenic-related health issues. We thank the Ministry of Science and Technology for funding this important initiative.",
                                                "https://images.unsplash.com/photo-1581244277943-fe4a9c777189?w=800",
                                                "Project Team"),

                                createNews("PhD Student Wins Best Presentation Award",
                                                "Salma Begum wins best oral presentation at the National Chemistry Conference.",
                                                "Congratulations to Salma Begum for winning the Best Oral Presentation Award at the 25th National Chemistry Conference held in Dhaka. Her presentation on 'Photocatalytic degradation of textile dyes using modified TiO2 nanoparticles' was highly appreciated by the jury for its innovative approach and practical implications for the textile industry. This is the third consecutive year our group members have won awards at this prestigious conference.",
                                                "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800",
                                                "Prof. Dr. Yunus Ahmed"),

                                createNews("New Laboratory Equipment Installation",
                                                "State-of-the-art High-Performance Liquid Chromatography system installed in our laboratory.",
                                                "Our laboratory has received a new Shimadzu HPLC system funded by the Higher Education Quality Enhancement Project (HEQEP). This advanced analytical instrument will significantly enhance our research capabilities in analyzing environmental pollutants and pharmaceutical compounds. Training sessions for group members will be conducted next week.",
                                                "https://images.unsplash.com/photo-1582719471384-894fbb16e074?w=800",
                                                "Lab Manager"));

                newsRepository.saveAll(newsItems);
                log.info("Created {} news items", newsItems.size());
        }

        private News createNews(String title, String summary, String content, String imageUrl, String author) {
                News news = new News();
                news.setTitle(title);
                news.setSummary(summary);
                news.setContent(content);
                news.setImageUrl(imageUrl);
                news.setAuthor(author);
                return news;
        }

        private void seedSiteSettings() {
                SiteSettings settings = new SiteSettings();
                settings.setId("default");
                settings.setGoogleScholarUrl("https://scholar.google.com/citations?user=5oILmB0AAAAJ&hl=en");
                settings.setAboutUs(
                                "Welcome to the Environmental Chemistry Research Group led by Prof. Dr. Yunus Ahmed at the Department of Chemistry, Chittagong University of Engineering & Technology (CUET), Bangladesh. Our research focuses on environmental remediation, resource recovery, nanomaterials, and cutting-edge wastewater treatment technologies. We are committed to developing sustainable solutions for environmental challenges facing Bangladesh and the world.");
                siteSettingsRepository.save(settings);
                log.info("Site settings initialized");
        }
}

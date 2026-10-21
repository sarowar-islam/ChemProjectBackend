package com.yunus.research.service;

import com.yunus.research.dto.ResearchFocusDto;
import com.yunus.research.entity.ResearchFocus;
import com.yunus.research.repository.ResearchFocusRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResearchFocusService {

    private final ResearchFocusRepository researchFocusRepository;

    @PostConstruct
    public void seedInitialDataIfEmpty() {
        try {
            if (researchFocusRepository.count() == 0) {
                log.info("Seeding initial Research Focus items into database...");

                List<ResearchFocus> initialFocus = List.of(
                        createFocusEntity(
                                "Environmental Remediation",
                                "Developing solutions for pollution control and environmental cleanup",
                                "<p>Environmental remediation is at the core of our research initiatives. We develop cost-effective, high-efficiency technologies targeting hazardous water and soil contaminants.</p>"
                                        + "<h3>Key Research Areas</h3>"
                                        + "<ul>"
                                        + "<li><strong>Advanced Oxidation Processes (AOPs):</strong> Fenton and photo-Fenton systems for degrading persistent organic pollutants.</li>"
                                        + "<li><strong>Heavy Metal Removal:</strong> Decontamination of industrial wastewater contaminated with arsenic, chromium, lead, and cadmium.</li>"
                                        + "<li><strong>Antibiotic Resistance Mitigation:</strong> Elimination of antibiotic-resistant bacteria (ARB) and antibiotic resistance genes (ARGs) from aquatic environments.</li>"
                                        + "</ul>"
                                        + "<p>Our group combines empirical laboratory investigations with computational modeling to optimize real-world decontamination efficiencies.</p>",
                                0),
                        createFocusEntity(
                                "Nanomaterials",
                                "Engineering nanoscale materials for water treatment and sensing",
                                "<p>Our laboratory designs, synthesizes, and characterizes advanced functional nanomaterials customized for environmental and chemical sensing applications.</p>"
                                        + "<h3>Key Innovations</h3>"
                                        + "<ul>"
                                        + "<li><strong>Photocatalytic Nanocomposites:</strong> Solar-responsive semiconductor nanomaterials tailored for rapid pollutant breakdown.</li>"
                                        + "<li><strong>Chemical & Electrochemical Sensors:</strong> High-sensitivity nanoscale sensors for trace pollutant and ion detection in water bodies.</li>"
                                        + "<li><strong>Magnetic Nanoadsorbents:</strong> Reusable magnetic nanocomposites enabling effortless magnetic separation after water treatment.</li>"
                                        + "</ul>"
                                        + "<p>We emphasize green synthesis pathways to ensure our nanomaterials are sustainable and environmentally benign.</p>",
                                1),
                        createFocusEntity(
                                "Wastewater Treatment",
                                "Cutting-edge technologies for industrial effluent treatment",
                                "<p>Rapid industrialization requires innovative and robust wastewater treatment approaches. Our research tackles real effluent streams from textile, tannery, pharmaceutical, and municipal sectors.</p>"
                                        + "<h3>Focus Domains</h3>"
                                        + "<ul>"
                                        + "<li><strong>Industrial Dye & Organic Degradation:</strong> Complete decolourization and mineralisation of toxic dyes.</li>"
                                        + "<li><strong>Integrated Treatment Trains:</strong> Synergistic pairing of biological and advanced chemical processes for zero liquid discharge (ZLD).</li>"
                                        + "<li><strong>Pilot-Scale Testing:</strong> Transitioning lab prototypes to pilot systems capable of continuous throughput.</li>"
                                        + "</ul>",
                                2),
                        createFocusEntity(
                                "Resource Recovery",
                                "Extracting valuable materials from waste streams and e-waste",
                                "<p>Championing the circular economy, our research aims to transform waste streams into valuable resources through selective recovery technologies.</p>"
                                        + "<h3>Core Research Directions</h3>"
                                        + "<ul>"
                                        + "<li><strong>Hydrometallurgical Recovery:</strong> Sustainable extraction of critical and precious metals from electronic waste (e-waste).</li>"
                                        + "<li><strong>Nutrient Harvesting:</strong> Recovering phosphorus, nitrogen, and potassium from industrial effluents and agricultural runoffs.</li>"
                                        + "<li><strong>Byproduct Valorization:</strong> Converting hazardous sludge into stable, high-value industrial precursors.</li>"
                                        + "</ul>",
                                3),
                        createFocusEntity(
                                "Adsorption Studies",
                                "Novel adsorbents for heavy metal and pollutant removal",
                                "<p>Adsorption offers an exceptionally versatile and energy-efficient route for contaminant removal. Our lab pioneers innovative bio-adsorbents and engineered carbons.</p>"
                                        + "<h3>Scientific Scope</h3>"
                                        + "<ul>"
                                        + "<li><strong>Agricultural Waste-Derived Biochar:</strong> Pyrolysis-engineered biochars with customized pore architectures and surface chemistries.</li>"
                                        + "<li><strong>Kinetic & Thermodynamic Modeling:</strong> In-depth evaluation of equilibrium isotherms, mass transfer dynamics, and interaction thermodynamics.</li>"
                                        + "<li><strong>Column and Packed-Bed Dynamics:</strong> Continuous flow column experiments to simulate realistic water treatment conditions.</li>"
                                        + "</ul>",
                                4),
                        createFocusEntity(
                                "Green Chemistry",
                                "Eco-friendly synthesis and sustainable chemical processes",
                                "<p>We adhere to the twelve principles of green chemistry, prioritizing energy efficiency, waste prevention, and renewable feedstocks across all research activities.</p>"
                                        + "<h3>Highlights</h3>"
                                        + "<ul>"
                                        + "<li><strong>Solvent-Free & Aqueous Reactions:</strong> Eliminating volatile organic solvents in chemical syntheses.</li>"
                                        + "<li><strong>Heterogeneous Catalysis:</strong> Recyclable heterogeneous catalysts reducing reaction times and energy requirements.</li>"
                                        + "<li><strong>Life Cycle Sustainability:</strong> Rigorous assessment of chemical toxicity and lifecycle impacts.</li>"
                                        + "</ul>",
                                5));

                researchFocusRepository.saveAll(initialFocus);
                log.info("Successfully seeded {} Research Focus items.", initialFocus.size());
            }
        } catch (Exception e) {
            log.error("Failed to seed Research Focus data: {}", e.getMessage(), e);
        }
    }

    private ResearchFocus createFocusEntity(String title, String shortDescription, String details, int displayOrder) {
        ResearchFocus focus = new ResearchFocus();
        focus.setTitle(title);
        focus.setShortDescription(shortDescription);
        focus.setDetails(details);
        focus.setDisplayOrder(displayOrder);
        return focus;
    }

    public List<ResearchFocusDto> getAllFocus() {
        return researchFocusRepository.findAllByOrderByDisplayOrderAscCreatedAtAsc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ResearchFocusDto> getFocusById(String id) {
        return researchFocusRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public ResearchFocusDto createFocus(ResearchFocusDto dto) {
        ResearchFocus focus = new ResearchFocus();
        focus.setTitle(dto.getTitle());
        focus.setShortDescription(dto.getShortDescription());
        focus.setDetails(dto.getDetails());
        if (dto.getDisplayOrder() != null) {
            focus.setDisplayOrder(dto.getDisplayOrder());
        } else {
            long count = researchFocusRepository.count();
            focus.setDisplayOrder((int) count);
        }

        ResearchFocus saved = researchFocusRepository.save(focus);
        return toDto(saved);
    }

    @Transactional
    public Optional<ResearchFocusDto> updateFocus(String id, ResearchFocusDto dto) {
        return researchFocusRepository.findById(id).map(focus -> {
            if (dto.getTitle() != null) {
                focus.setTitle(dto.getTitle());
            }
            if (dto.getShortDescription() != null) {
                focus.setShortDescription(dto.getShortDescription());
            }
            if (dto.getDetails() != null) {
                focus.setDetails(dto.getDetails());
            }
            if (dto.getDisplayOrder() != null) {
                focus.setDisplayOrder(dto.getDisplayOrder());
            }
            return toDto(researchFocusRepository.save(focus));
        });
    }

    @Transactional
    public boolean deleteFocus(String id) {
        if (researchFocusRepository.existsById(id)) {
            researchFocusRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ResearchFocusDto toDto(ResearchFocus focus) {
        return new ResearchFocusDto(
                focus.getId(),
                focus.getTitle(),
                focus.getShortDescription(),
                focus.getDetails(),
                focus.getDisplayOrder(),
                focus.getCreatedAt() != null ? focus.getCreatedAt().toString() : null,
                focus.getUpdatedAt() != null ? focus.getUpdatedAt().toString() : null);
    }
}

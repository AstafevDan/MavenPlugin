package org.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Developer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * Maven plugin, который во время вызова фазы compile выводит информацию в консоль о разработчиках проекта, перечисленных в теге
 * {@code <developers>}.
 * <p>
 * В качестве параметра в командной строке (-Dorg) можно передать название организации, если хотите получить информацию
 * только о разработчиках конкретной организации.
 * </p>
 *
 * @author Даниил Астафьев
 * @version 1.0
 */
@Mojo(name = "developers-info", defaultPhase = LifecyclePhase.COMPILE)
public class DevInfoMojo extends AbstractMojo {

    /**
     * Предоставление информации о проекте Maven.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /**
     * Параметр, в котором можно указать название организации.
     * {@code -Dorg=<organization>}
     */
    @Parameter(property = "org")
    private String org;

    /**
     * Основной метод, выводящий информацию о разработчике (id, имя, электронная почта, url, организация, роли) в консоль.
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<Developer> developers = project.getDevelopers();
        if (developers == null || developers.isEmpty()) {
            getLog().info("No developers found");
        } else {
            getLog().info("Information about project's developers\n");
            developers.stream()
                    .filter(developer -> (StringUtils.isBlank(org) || StringUtils.equals(org, developer.getOrganization())))
                    .forEach(developer -> {
                        getLog().info("Developer ID: " + developer.getId());
                        getLog().info("Name: " + developer.getName());
                        getLog().info("Email: " + developer.getEmail());
                        getLog().info("URL: " + developer.getUrl());
                        getLog().info("Organization: " + developer.getOrganization());
                        getLog().info("Organization URL: " + developer.getOrganizationUrl());
                        getLog().info("Roles: " + developer.getRoles() + "\n");
                    });

        }
    }
}

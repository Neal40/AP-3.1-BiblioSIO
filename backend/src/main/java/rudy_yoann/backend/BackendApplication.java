package rudy_yoann.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import rudy_yoann.backend.Article.Article;
import rudy_yoann.backend.Article.ArticleRepository;
import rudy_yoann.backend.DemandeEmprunt.DemandeEmprunt;
import rudy_yoann.backend.DemandeEmprunt.DemandeEmpruntRepository;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunt.EmpruntRepository;
import rudy_yoann.backend.Emprunteur.Classe;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Emprunteur.EmprunteurRepository;
import rudy_yoann.backend.Emprunteur.Role;
import rudy_yoann.backend.Exemplaire.Exemplaire;
import rudy_yoann.backend.Exemplaire.ExemplaireRepository;
import rudy_yoann.backend.Exemplaire.Statut;
import rudy_yoann.backend.Revue.Revue;
import rudy_yoann.backend.Revue.RevueRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static rudy_yoann.backend.Emprunt.Statut.EN_COURS;
import static rudy_yoann.backend.Emprunt.Statut.TERMINE;

@EnableScheduling
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner setUpBDD(RevueRepository revueRepository, ExemplaireRepository exemplaireRepository, EmpruntRepository empruntRepository, EmprunteurRepository emprunteurRepository, ArticleRepository articleRepository, DemandeEmpruntRepository demandeEmpruntRepository) {
		return (args) -> {

			Revue revue1 = new Revue(1L, "titre1");
			Revue revue2 = new Revue(2L, "titre2");
			Revue revue3 = new Revue(3L, "titre3");
			List<Revue> revues = new ArrayList<>(){{
				add(revue1);
				add(revue2);
				add(revue3);
			}};
			revueRepository.saveAll(revues);


			Exemplaire exemplaire1 = new Exemplaire(1L, revue1, "titre1", "Janvier", "2001", Statut.DISPONIBLE);
			Exemplaire exemplaire2 = new Exemplaire(2L, revue2, "titre2", "Février", "2002", Statut.INDISPONIBLE);
			Exemplaire exemplaire3 = new Exemplaire(3L, revue3, "titre3", "Mars", "2003", Statut.DISPONIBLE);
			List<Exemplaire> exemplaires = new ArrayList<>() {{
				add(exemplaire1);
				add(exemplaire2);
				add(exemplaire3);
			}};
			exemplaireRepository.saveAll(exemplaires);


			Emprunteur emprunteur1 = new Emprunteur(1L, "Nom1", "Prenom1", "Mail1", Role.etudiant, Classe.SIO1B,"2023", rudy_yoann.backend.Emprunteur.Statut.NON_BLOQUE);
			Emprunteur emprunteur2 = new Emprunteur(2L, "Nom2", "Prenom2", "Mail2",  Role.etudiant, Classe.SIO2A,"2023", rudy_yoann.backend.Emprunteur.Statut.NON_BLOQUE);
			Emprunteur emprunteur3 = new Emprunteur(3L, "Nom3", "Prenom3", "Mail3", Role.professeur, Classe.PROF, null, rudy_yoann.backend.Emprunteur.Statut.BLOQUE);
			List<Emprunteur> emprunteurs = new ArrayList<>() {{
				add(emprunteur1);
				add(emprunteur2);
				add(emprunteur3);
			}};
			emprunteurRepository.saveAll(emprunteurs);


			Emprunt emprunt1 =new Emprunt(1L, java.sql.Date.valueOf("2023-01-01"), null, rudy_yoann.backend.Emprunt.Statut.EN_COURS, emprunteur1, exemplaire1,1);
			Emprunt emprunt2 = new Emprunt(2L, java.sql.Date.valueOf("2023-02-01"), null, rudy_yoann.backend.Emprunt.Statut.EN_COURS, emprunteur2, exemplaire2,6);
			Emprunt emprunt3 = new Emprunt(3L, java.sql.Date.valueOf("2023-03-01"), java.sql.Date.valueOf("2023-03-15"), rudy_yoann.backend.Emprunt.Statut.TERMINE, emprunteur3, exemplaire3,15);
			List<Emprunt> emprunts = new ArrayList<>() {{
				add(emprunt1);
				add(emprunt2);
				add(emprunt3);
			}};
			empruntRepository.saveAll(emprunts);


			Article article1 = new Article(1L, "titre1", "desc1", exemplaire1);
			Article article2 = new Article(2L, "titre2", "desc2", exemplaire2);
			Article article3 = new Article(3L, "titre3", "desc3", exemplaire3);
			List<Article> articles = new ArrayList<>() {{
				add(article1);
				add(article2);
				add(article3);
			}};
			articleRepository.saveAll(articles);


			DemandeEmprunt demandeEmprunt1 = new DemandeEmprunt(1L, java.sql.Date.valueOf("2023-01-01"), java.sql.Date.valueOf("2023-01-10"), rudy_yoann.backend.DemandeEmprunt.Statut.EN_COURS, exemplaire1 , emprunteur1);
			DemandeEmprunt demandeEmprunt2 = new DemandeEmprunt(2L, java.sql.Date.valueOf("2023-02-02"), java.sql.Date.valueOf("2023-02-20"), rudy_yoann.backend.DemandeEmprunt.Statut.VALIDEE, exemplaire2 , emprunteur2);
			DemandeEmprunt demandeEmprunt3 = new DemandeEmprunt(3L, java.sql.Date.valueOf("2023-03-03"), java.sql.Date.valueOf("2023-03-30"), rudy_yoann.backend.DemandeEmprunt.Statut.REJETEE, exemplaire3 , emprunteur3);
			List<DemandeEmprunt> demandeEmprunts = new ArrayList<>(){{
				add(demandeEmprunt1);
				add(demandeEmprunt2);
				add(demandeEmprunt3);
			}};
			demandeEmpruntRepository.saveAll(demandeEmprunts);

		};
	}
}

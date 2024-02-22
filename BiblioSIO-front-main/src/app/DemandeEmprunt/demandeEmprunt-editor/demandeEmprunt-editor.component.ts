import {Component, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {DemandeEmprunt, StatutDemandeEmprunt} from "../DemandeEmprunt";
import {DemandeEmpruntService} from "../demandeEmprunt.service";
import {Emprunteur} from "../../Emprunteur/Emprunteur";
import {Exemplaire} from "../../Exemplaire/Exemplaire";
import {EmprunteurService} from "../../Emprunteur/emprunteur.service";
import {ExemplaireService} from "../../Exemplaire/exemplaire.service";
import {Observable} from "rxjs";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-demandeEmprunt-editor',
  templateUrl: './demandeEmprunt-editor.component.html',
  styleUrls: ['./demandeEmprunt-editor.component.css']
})
export class DemandeEmpruntEditorComponent {
  demandeEmprunt: DemandeEmprunt = {} as DemandeEmprunt
  updating: boolean = false
  creating: boolean = false
  emprunteurs$!: Observable<Emprunteur[]>
  exemplaires$!: Observable<Exemplaire[]>
  statuts = Object.values(StatutDemandeEmprunt)
  @ViewChild('myForm') myForm!: NgForm;

  constructor(
    private demandeEmpruntService: DemandeEmpruntService,
    private emprunteurService: EmprunteurService,
    private exemplaireService: ExemplaireService,
    private router: Router
  ) {}

  ngOnInit() {
    if (history.state.demandeEmprunt!=null){
      this.demandeEmprunt=history.state.demandeEmprunt
    }
    this.creating = history.state.creating
    this.updating = history.state.updating
    this.emprunteurs$=this.emprunteurService.get()
    this.exemplaires$=this.exemplaireService.get()
  }

  edit() {
    if(this.updating) {
      console.log(this.myForm.value)
      console.log(this.demandeEmprunt)
      this.demandeEmpruntService.update(({id :this.demandeEmprunt.id, statut: this.demandeEmprunt.statut} as DemandeEmprunt))
        .subscribe(()=>this.processUpdate())
    } else if (this.creating) {
      this.demandeEmpruntService.create(this.demandeEmprunt)
        .subscribe((location)=>this.processCreate(location))
    }
  }

  processUpdate() {
    this.router.navigate(["/demandeEmprunts/"+this.demandeEmprunt.id],
      {state: {demandeEmprunt: this.demandeEmprunt}})
  }

  processCreate(url: string) {
    console.log(url)
    this.router.navigate([url])
  }
}

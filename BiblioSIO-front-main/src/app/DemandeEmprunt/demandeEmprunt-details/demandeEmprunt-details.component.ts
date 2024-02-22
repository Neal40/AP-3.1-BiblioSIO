import {Component, Input} from '@angular/core';
import {DemandeEmprunt} from "../../DemandeEmprunt/DemandeEmprunt";
import {DemandeEmpruntService} from "../../DemandeEmprunt/demandeEmprunt.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-demandeEmprunt-details',
  templateUrl: './demandeEmprunt-details.component.html',
  styleUrls: ['./demandeEmprunt-details.component.css']
})
export class DemandeEmpruntDetailsComponent {
  @Input() demandeEmprunt!: DemandeEmprunt
  @Input() solo: boolean = true

  constructor(
    private demandeEmpruntService: DemandeEmpruntService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(){
    if(this.demandeEmprunt==null) {
      this.demandeEmprunt=history.state.emprunt
      if (history.state.demandeEmprunt==null) {
        this.demandeEmpruntService.getById(Number(this.activatedRoute.snapshot.paramMap.get("id")))
          .subscribe(demandeEmprunt=>this.demandeEmprunt=demandeEmprunt)
      }
    }
    this.solo=history.state.solo
  }

  delete(){
    this.demandeEmpruntService.delete(this.demandeEmprunt.id)
      .subscribe(()=>this.router.navigate(['/demandeEmprunts']))
  }

  update() {
    this.router.navigate(["/demandeEmprunts/edit"],
      {state: {demandeEmprunt: this.demandeEmprunt,
          updating: true}})
  }
}

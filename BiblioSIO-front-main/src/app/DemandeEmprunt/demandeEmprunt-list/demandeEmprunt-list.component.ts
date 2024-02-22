import { Component } from '@angular/core';
import {DemandeEmprunt} from "../../DemandeEmprunt/DemandeEmprunt";
import {DemandeEmpruntService} from "../../DemandeEmprunt/demandeEmprunt.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-demandeEmprunt-list',
  templateUrl: './demandeEmprunt-list.component.html',
  styleUrls: ['./demandeEmprunt-list.component.css']
})
export class DemandeEmpruntListComponent {
  demandeEmprunts: DemandeEmprunt[]

  constructor(
    private demandeEmpruntService: DemandeEmpruntService,
    private router: Router
  ) {
    this.demandeEmprunts = []
    demandeEmpruntService.get()
      .subscribe((demandesEmprunts)=>this.demandeEmprunts=demandesEmprunts)
  }

  openDemandeEmpruntDetails(demandeEmprunt: DemandeEmprunt) {
    this.router.navigate(['/demandeEmprunts/'+demandeEmprunt.id],
      {state: {demandeEmprunt: demandeEmprunt, solo: true}})
  }

  openCreateDemandeEmprunt() {
    this.router.navigate(['/demandeEmprunts/edit'],
      {state: {creating: true}})
  }
}

import { Injectable } from '@angular/core';
import {GenericService} from "../utils/generic.service";
import {DemandeEmprunt} from "./DemandeEmprunt";

@Injectable({
  providedIn: 'root'
})
export class DemandeEmpruntService extends GenericService<DemandeEmprunt>{

  protected override className = "DemandeEmprunt"
  protected override url = "http://localhost:8080/demandeEmprunts"

}

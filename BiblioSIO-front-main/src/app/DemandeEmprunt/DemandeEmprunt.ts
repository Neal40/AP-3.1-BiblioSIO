import {SerializedUrlObject} from "../utils/SerializedUrlObject";

export interface DemandeEmprunt {
  id: Number,
  emprunteur: SerializedUrlObject,
  exemplaire: SerializedUrlObject,
  dateCreation: Date,
  dateTraitement: Date,
  statut: StatutDemandeEmprunt
}

export enum StatutDemandeEmprunt {
  EN_COURS = "EN_COURS",
  VALIDEE = "VALIDEE",
  REJETEE = "REJETEE"
}

import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ThirdParty } from 'src/app/models/third-party';
import { ThirdPartyService } from 'src/app/services/third-party.service';

@Component({
  selector: 'app-third-parties',
  templateUrl: './third-parties.component.html',
  styleUrls: ['./third-parties.component.scss']
})
export class ThirdPartiesComponent implements OnInit {

  public thirdParties$!: Observable<ThirdParty[]>;

  constructor(
    private thirdPartyService: ThirdPartyService
  ) { }

  ngOnInit(): void {
    this.getThirdParties();
  }

  deleteThirdParty(id: string | undefined): void {
    if (confirm("Are you sure?") && id) {
      this.thirdPartyService.deleteThirdParty(id).subscribe(() => this.getThirdParties());
    }
  }

  getThirdParties() {
    this.thirdParties$ = this.thirdPartyService.getAll();
  }
}

import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-layout-two-columns',
  templateUrl: './layout-two-columns.component.html',
  styleUrls: ['./layout-two-columns.component.scss']
})
export class LayoutTwoColumnsComponent implements OnInit {

  @Input() leftWidth: number = 100;

  constructor() { }

  ngOnInit(): void {
  }

}

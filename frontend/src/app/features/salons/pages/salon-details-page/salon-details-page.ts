import { Component, inject, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { SalonsApiService } from '../../../../core/api/salons-api.service';
import { SalonDetails } from '../../models/salon.model';

@Component({
  selector: 'app-salon-details-page',
  imports: [
    NgFor,
    NgIf,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatDividerModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './salon-details-page.html',
  styleUrl: './salon-details-page.scss',
})
export class SalonDetailsPage implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly salonsApiService = inject(SalonsApiService);

  salon: SalonDetails | null = null;
  isLoading = true;
  errorMessage: string | null = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id) {
      this.errorMessage = 'Invalid salon id.';
      this.isLoading = false;
      return;
    }

    this.loadSalon(id);
  }

  private loadSalon(id: number): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.salonsApiService.getSalon(id).subscribe({
      next: salon => {
        this.salon = salon;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Could not load salon details.';
        this.isLoading = false;
      },
    });
  }
}

import { Component, inject, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { SalonsApiService } from '../../../../core/api/salons-api.service';
import { SalonDetails, UpdateSalonRequest } from '../../models/salon.model';

@Component({
  selector: 'app-salon-details-page',
  imports: [
    NgFor,
    NgIf,
    RouterLink,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatDividerModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './salon-details-page.html',
  styleUrl: './salon-details-page.scss',
})
export class SalonDetailsPage implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly salonsApiService = inject(SalonsApiService);

  salon: SalonDetails | null = null;
  editForm: UpdateSalonRequest = {};

  isEditing = false;
  isSaving = false;
  isLoading = true;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id) {
      this.errorMessage = 'Invalid salon id.';
      this.isLoading = false;
      return;
    }

    this.loadSalon(id);
  }

  startEdit(): void {
    if (!this.salon) {
      return;
    }

    this.successMessage = null;
    this.errorMessage = null;

    this.editForm = {
      name: this.salon.name,
      address: this.salon.address,
      district: this.salon.district,
      phoneNumber: this.salon.phoneNumber,
      websiteUrl: this.salon.websiteUrl,
      priceRange: this.salon.priceRange,
      rating: this.salon.rating,
      reviewCount: this.salon.reviewCount,
      services: [...this.salon.services],
    };

    this.isEditing = true;
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.successMessage = null;
  }

  saveChanges(): void {
    if (!this.salon) {
      return;
    }

    this.isSaving = true;
    this.errorMessage = null;
    this.successMessage = null;

    this.salonsApiService.updateSalon(this.salon.id, this.editForm).subscribe({
      next: updatedSalon => {
        this.salon = updatedSalon;
        this.isEditing = false;
        this.isSaving = false;
        this.successMessage = 'Salon details saved successfully.';
      },
      error: () => {
        this.errorMessage = 'Could not save salon details.';
        this.isSaving = false;
      },
    });
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

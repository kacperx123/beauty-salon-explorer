import { Component, inject, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SalonsApiService } from '../../../../core/api/salons-api.service';
import { SalonListItem } from '../../models/salon.model';

@Component({
  selector: 'app-salon-list-page',
  imports: [NgFor, NgIf, MatCardModule, MatProgressSpinnerModule],
  templateUrl: './salon-list-page.html',
  styleUrl: './salon-list-page.scss',
})
export class SalonListPage implements OnInit {
  private readonly salonsApiService = inject(SalonsApiService);

  salons: SalonListItem[] = [];
  isLoading = true;
  errorMessage: string | null = null;
  totalElements = 0;

  ngOnInit(): void {
    this.loadSalons();
  }

  private loadSalons(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.salonsApiService.getSalons({
      page: 0,
      size: 20,
      sortBy: 'name',
      direction: 'asc',
    }).subscribe({
      next: response => {
        this.salons = response.content;
        this.totalElements = response.totalElements;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Could not load salons. Please check if backend API is running.';
        this.isLoading = false;
      },
    });
  }
}

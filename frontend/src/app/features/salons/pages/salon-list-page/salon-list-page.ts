import { Component, inject, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';

import { SalonsApiService } from '../../../../core/api/salons-api.service';
import { SalonListItem } from '../../models/salon.model';

@Component({
  selector: 'app-salon-list-page',
  imports: [
    NgFor,
    NgIf,
    FormsModule,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatSelectModule,
  ],
  templateUrl: './salon-list-page.html',
  styleUrl: './salon-list-page.scss',
})
export class SalonListPage implements OnInit {
  private readonly salonsApiService = inject(SalonsApiService);

  salons: SalonListItem[] = [];
  districts: string[] = [];
  services: string[] = [];

  selectedDistrict: string | null = null;
  selectedService: string | null = null;

  page = 0;
  size = 20;
  totalElements = 0;

  isLoading = true;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadFilters();
    this.loadSalons();
  }

  loadSalons(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.salonsApiService.getSalons({
      district: this.selectedDistrict,
      service: this.selectedService,
      page: this.page,
      size: this.size,
      sortBy: 'name',
      direction: 'asc',
    }).subscribe({
      next: response => {
        this.salons = response.content;
        this.totalElements = response.totalElements;
        this.page = response.page;
        this.size = response.size;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Could not load salons. Please check if backend API is running.';
        this.isLoading = false;
      },
    });
  }

  onFiltersChange(): void {
    this.page = 0;
    this.loadSalons();
  }

  clearFilters(): void {
    this.selectedDistrict = null;
    this.selectedService = null;
    this.page = 0;
    this.loadSalons();
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.loadSalons();
  }

  private loadFilters(): void {
    this.salonsApiService.getDistricts().subscribe({
      next: districts => {
        this.districts = districts;
      },
    });

    this.salonsApiService.getServices().subscribe({
      next: services => {
        this.services = services;
      },
    });
  }
}

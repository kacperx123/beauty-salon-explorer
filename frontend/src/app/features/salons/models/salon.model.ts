export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface SalonListItem {
  id: number;
  name: string;
  district: string;
  rating: number | null;
  reviewCount: number | null;
  priceRange: string | null;
  services: string[];
}

export interface SalonDetails {
  id: number;
  name: string;
  address: string;
  district: string;
  phoneNumber: string | null;
  websiteUrl: string | null;
  services: string[];
  priceRange: string | null;
  rating: number | null;
  reviewCount: number | null;
  source: string | null;
  latitude: number | null;
  longitude: number | null;
}

export interface UpdateSalonRequest {
  name?: string;
  address?: string;
  district?: string;
  phoneNumber?: string | null;
  websiteUrl?: string | null;
  services?: string[];
  priceRange?: string | null;
  rating?: number | null;
  reviewCount?: number | null;
}

export interface SalonSearchParams {
  district?: string | null;
  service?: string | null;
  page?: number;
  size?: number;
  sortBy?: 'name' | 'district' | 'rating' | 'priceRange';
  direction?: 'asc' | 'desc';
}

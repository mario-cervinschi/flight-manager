import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface User {
  id: string;
  name: string;
  email: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    this.checkExistingAuth();
  }

  private checkExistingAuth() {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUserSubject.next(JSON.parse(savedUser));
    }
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  get isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }

  login(email: string, password: string): Promise<User> {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const mockUsers = [
          {
            id: '1',
            name: 'John Doe',
            email: 'john@example.com',
            password: '123456',
          },
          {
            id: '2',
            name: 'Jane Smith',
            email: 'jane@example.com',
            password: 'password',
          },
          {
            id: '3',
            name: 'Admin User',
            email: 'admin@example.com',
            password: 'admin123',
          },
        ];

        const foundUser = mockUsers.find(
          (user) => user.email === email && user.password === password
        );

        if (foundUser) {
          const user: User = {
            id: foundUser.id,
            name: foundUser.name,
            email: foundUser.email,
          };

          const mockToken = `mock-jwt-token-${Date.now()}`;
          localStorage.setItem('authToken', mockToken);
          localStorage.setItem('currentUser', JSON.stringify(user));

          this.currentUserSubject.next(user);
          resolve(user);
        } else {
          reject(new Error('Invalid email or password'));
        }
      }, 1000);
    }) as any;
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('authToken');
    this.currentUserSubject.next(null);
  }
}

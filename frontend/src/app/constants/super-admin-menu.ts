import { NbMenuItem } from "@nebular/theme";

export const SUPER_ADMIN_MENU: NbMenuItem[] = [
  {
    title: 'Admins',
    icon: 'briefcase-outline',
    link: '/admins',
    selected: true,
    home: true
  },
  {
    title: 'Users',
    icon: 'people-outline',
    link: '/users'
  },
]
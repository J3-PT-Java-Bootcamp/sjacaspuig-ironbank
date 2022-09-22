import { NbMenuItem } from "@nebular/theme";

export const ADMIN_MENU: NbMenuItem[] = [
  {
    title: 'Account holders',
    icon: 'people-outline',
    link: '/users',
    selected: true,
    home: true
  },
  {
    title: 'Third parties',
    icon: 'external-link-outline',
    link: '/third-parties',
  },
  {
    title: 'Accounts',
    icon: 'lock-outline',
    link: '/accounts/creation',
  },
  {
    title: 'Transactions',
    icon: 'globe-outline',
    link: '/transactions/creation',
  },
]
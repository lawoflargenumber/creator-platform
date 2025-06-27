<script setup>
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'
</script>

<template>
    <ul>
        <VerticalNavSectionTitle :item="{ heading: 'Account' }" />
        <VerticalNavLink
            :item="{
                title: 'Users',
                to: '/users',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: 'Writing' }" />
        <VerticalNavLink
            :item="{
                title: 'Drafts',
                to: '/drafts',
            }"
        />
        <VerticalNavLink
            :item="{
                title: 'CheckAuthors',
                to: '/checkAuthors',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: 'Publish' }" />
        <VerticalNavLink
            :item="{
                title: 'Products',
                to: '/products',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: 'View' }" />
        <VerticalNavLink
            :item="{
                title: 'UserAccessProfile',
                to: '/userAccessProfiles',
            }"
        />
        <VerticalNavLink
            :item="{
                title: 'CheckPrice',
                to: '/checkPrices',
            }"
        />
        <VerticalNavLink
            :item="{
                title: 'CheckIfBought',
                to: '/checkIfBoughts',
            }"
        />
        <VerticalNavLink
            :item="{
                title: 'ViewHistory',
                to: '/viewHistories',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: 'Ai' }" />
        <VerticalNavLink
            :item="{
                title: '',
                to: '/',
            }"
        />
    </ul>
</template>
